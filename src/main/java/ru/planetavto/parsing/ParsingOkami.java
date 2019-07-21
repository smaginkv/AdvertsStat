package ru.planetavto.parsing;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.planetavto.advertsment.AdvertImage;
import ru.planetavto.advertsment.Price;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.messaging.JmsAdvertMessagingService;
import ru.planetavto.presistent.CarAdvertService;
import ru.planetavto.presistent.ParsingPlanService;

@Component
public class ParsingOkami {

	private CarAdvertService advertRepo;
	private ParsingPlanService planRepo;
	private JmsAdvertMessagingService messaging;
	
	@Autowired
	public ParsingOkami(CarAdvertService advertRepo, ParsingPlanService planRepo, JmsAdvertMessagingService messaging) {
		this.advertRepo = advertRepo;
		this.planRepo = planRepo;
		this.messaging = messaging;
	}

	private CarAdvert advert;

	private String advertSeparator = "col-lg-3 col-md-4 col-sm-6";
	private String pathToImage = "https://photos.okami-market.ru/images/auto/large/";
	private String primaryImage = "-exterior-front.jpg";
	private String imageSeparator = "fancybox";

	public void parseAdvertsByAllPlans() {

		List<ParsingPlan> plans = planRepo.findAllActive();
		for (ParsingPlan plan : plans) {
			
			parseAdvertByPlan(plan);
			plan.setLastInvoke(LocalDateTime.now());
			planRepo.save(plan);
		}		
	}
	
	public void parseImagesByAdvert(long advertId) {
		Document doc;
		advert = advertRepo.findById(advertId);

		try {
			doc = Jsoup.connect(advert.getPlan().getPathToAdvert() + advert.getRef()).get();

		} catch (IOException e) {
			// write exception to log
			return;
		}
		
		Elements imageItems = doc.body().getElementsByClass(imageSeparator);
		List<AdvertImage> images = new ArrayList<>();
		for (Element imageItem : imageItems) {

			try {				
				String pathToImage = imageItem.attr("href");
				
				AdvertImage image = new AdvertImage();
				image.setTitle("0");
				image.setImage(getImage(pathToImage));
				images.add(image);
			} catch (ParseException e) {
				// write exception to log
			}
		}
		advert.setImages(images);
		advertRepo.save(advert);
	}
	
	private List<CarAdvert> parseAdvertByPlan(ParsingPlan plan) {
		Document doc;
		List<CarAdvert> planList = new ArrayList<>(), sreenList = new ArrayList<>();
		int index = 1;
		do {
			
			try {
				doc = Jsoup.connect(plan.getUrl()+index++).get();

			} catch (IOException e) {
				// write exception to log
				continue;
			}
			sreenList = getAdvertScreenList(doc, plan);
			planList.addAll(sreenList);
			
		} while ( !sreenList.isEmpty());
		
		long[] ids = advertRepo.setInactiveForAllExceptAdvertList(planList, plan);
		SendMessageBecomeInactive(ids);
		
		return planList;
		
	}
	private List<CarAdvert> getAdvertScreenList(Document doc, ParsingPlan plan) {
		List<CarAdvert> advertList = new ArrayList<CarAdvert>();
		
		Elements carsDiv = doc.body().getElementsByClass(advertSeparator);
		for (Element carDiv : carsDiv) {
			try {
				advert = getAdvert(carDiv);
				
				setAdvertImage();
				setEngineCapacity(carDiv);
				setProductionYear(carDiv);
				setMilage(carDiv);
				setPrice(carDiv);				
				advert.setModel(plan.getModel());
				advert.setPlan(plan);
				
				advertRepo.save(advert);
				
				advertList.add(advert);
			} catch (ParseException e) {
				// write exception to log
			}
		}
		return advertList;		
	}

	private String getRef(Element carDiv) throws ParseException {

		String draftRref = carDiv.getElementsByAttribute("href").first().attr("href");

		Pattern pattern = Pattern.compile(".+?/");
		Matcher matcher = pattern.matcher(draftRref);
		int start = 0;
		int end = 0;
		while (matcher.find()) {
			start = matcher.start();
			end = matcher.end() - 1;
		}

		if (start == end) {
			throw new ParseException("Unable to define ref from'" + draftRref + "'", 0);
		}
		return draftRref.substring(start, end);

	}

	private CarAdvert getAdvert(Element carDiv) throws ParseException {

		String ref = getRef(carDiv);
		try {
			advert = advertRepo.findByRef(ref);
		} catch (EntityNotFoundException e) {
			advert = new CarAdvert();
			advert.setRef(ref);
			//change invoke to AOP when generate id
			//need to intermediate structure that have all information about new advert
			messaging.sendAdvert("new advert");
		}
		return advert;
	}

	private void setAdvertImage() {
		String url = pathToImage + advert.getRef() + primaryImage;
		try {
			advert.setImage(getImage(url));
		} catch (ParseException e) {
			// write exception to log
		}
	}
	
	private byte[] getImage(String strUrl) throws ParseException {
		ByteArrayOutputStream jpgContent = new ByteArrayOutputStream();
		try {

			URL url = new URL(strUrl);
			BufferedImage inputImage = ImageIO.read(url.openStream());
			ImageIO.write(inputImage, "jpg", jpgContent);

		} catch (IOException e) {
			throw new ParseException(e.getMessage(), 0);
			
		}catch(IllegalArgumentException e) {
			//no image, use default
			throw new ParseException(e.getMessage(), 0);
		}
		
		return jpgContent.toByteArray();		
	}

	private void setEngineCapacity(Element carDiv) {
		String draftEngineCapacity = carDiv.getElementsByClass("auto-short-description").first().text();

		Pattern pattern = Pattern.compile(".+?\\s");
		Matcher matcher = pattern.matcher(draftEngineCapacity);

		if (matcher.find()) {
			advert.setEngineCapacity(Float.parseFloat(matcher.group()));
		}

	}

	private void setProductionYear(Element carDiv) throws ParseException {
		String draftProductionYear = carDiv.getElementsByClass("auto-name").first().text();

		Pattern pattern = Pattern.compile("\\s\\d{4}");
		Matcher matcher = pattern.matcher(draftProductionYear);
		int start = 0;
		int end = 0;
		while (matcher.find()) {
			start = matcher.start() + 1;
			end = matcher.end();
		}

		if (start == end) {
			throw new ParseException("Unable to define production year from'" + draftProductionYear + "'", 0);
		}

		String productionYear = draftProductionYear.substring(start, end);
		advert.setProductionYear(Integer.parseInt(productionYear));
	}

	private void setMilage(Element carDiv) {
		String draftMilage = carDiv.getElementsByClass("auto-short-description").first().text();

		Pattern pattern = Pattern.compile("\\d+?\\sкм");
		Matcher matcher = pattern.matcher(draftMilage);

		if (matcher.find()) {
			String milage = draftMilage.substring(matcher.start(), matcher.end() - 3);
			advert.setMileage(Integer.parseInt(milage));
		}
	}
	
	private void setPrice(Element carDiv) {
		String draftPrice = carDiv.getElementsByClass("price").first().text();
		
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(draftPrice);		
		int newPrice = Integer.parseInt(matcher.replaceAll(""));
		
		if (!advert.lastPriceEquals(newPrice)) {
			
			//use AOP to check changes
			String message = String.format("There is update price! ID: '%d' from %d to %d", advert.getId(),
					advert.getLastPrice(), newPrice);
			messaging.sendAdvert(message);
			
			List<Price> prices = advert.getPrices();
			prices.add(new Price(newPrice));
		}
	}
	
	private void SendMessageBecomeInactive(long []ids) {
		for(long id: ids) {
			String message = String.format("Advert become inactive! ID: '%d'", id);
			messaging.sendAdvert(message);
		}	
	}
}
