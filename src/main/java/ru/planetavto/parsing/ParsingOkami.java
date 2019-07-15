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

import ru.planetavto.advertsment.Price;
import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.CarAdvertService;
import ru.planetavto.presistent.ModelRepository;
import ru.planetavto.presistent.ParsingPlanService;

@Component
public class ParsingOkami {

	@Autowired
	private ModelRepository modelRepo;

	@Autowired
	private CarAdvertService advertRepo;
	
	@Autowired
	private ParsingPlanService planRepo;

	private CarAdvert advert;

	private String advertSeparator = "col-lg-3 col-md-4 col-sm-6";
	private String pathToImage = "https://photos.okami-market.ru/images/auto/large/";
	private String primaryImage = "-exterior-front.jpg";

	public List<CarAdvert> getAdvertList() {		
		Document doc;
		List<CarAdvert> srcList = new ArrayList<CarAdvert>();
		List<CarAdvert> advertList = new ArrayList<CarAdvert>();
		
		List<ParsingPlan> plans = planRepo.findAllActive();		
		for (ParsingPlan plan: plans) {
			int index = 1;
			do {
				try {
					doc = Jsoup.connect(plan.getUrl()+index++).get();

				} catch (IOException e) {
					// write exception to log
					continue;
				}
				srcList = parseAdvertList(doc, plan);
				advertList.addAll(srcList);
				
			} while ( !srcList.isEmpty());
			
			plan.setLastInvoke(LocalDateTime.now());
			planRepo.save(plan);
		}		
		return advertList;
	}
	
	private List<CarAdvert> parseAdvertList(Document doc, ParsingPlan plan) {
		List<CarAdvert> advertList = new ArrayList<CarAdvert>();
		
		Elements carsDiv = doc.body().getElementsByClass(advertSeparator);
		for (Element carDiv : carsDiv) {
			try {
				advert = getAdvert(carDiv);
				
				setImage();
				setEngineCapacity(carDiv);
				setProductionYear(carDiv);
				setMilage(carDiv);
				setPrice(carDiv);
				
				advert.setModel(modelRepo.findById(1l));
				advert.setPlan(plan);
				
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
		}
		return advert;
	}

	private void setImage() {
		String rref = advert.getRef();
		try {

			URL url = new URL(pathToImage + rref + primaryImage);
			BufferedImage inputImage = ImageIO.read(url.openStream());
			ByteArrayOutputStream jpgContent = new ByteArrayOutputStream();

			ImageIO.write(inputImage, "jpg", jpgContent);
			advert.setImage(jpgContent.toByteArray());

		} catch (IOException e) {
			// write exception to log
			
		}catch(IllegalArgumentException e) {
			//no image, use default
		}
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
		int cost = Integer.parseInt(matcher.replaceAll(""));
		
		if (!advert.lastPriceEquals(cost)) {
			List<Price> prices = advert.getPrices();
			prices.add(new Price(cost));
		}		
	}
}
