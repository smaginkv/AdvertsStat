package ru.planetavto.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.planetavto.advertsment.car.CarAdvert;
import ru.planetavto.presistent.CarAdvertService;
import ru.planetavto.presistent.ModelRepository;

@Controller
@RequestMapping("/parse")
public class ParsingController {

	@Autowired
	private CarAdvertService advertRepo;
	@Autowired
	private ModelRepository modelRepo;

	@GetMapping
	public String registerForm() {
		return "parsing";
	}

	@PostMapping
	public String processRegistration() throws IOException, SQLException {
		String adress = "https://okami-market.ru/poisk-more/?poisk%5Bbrand_id%5D=null&poisk%5Byear_from%5D=null&poisk%5"
				+ "Byear_to%5D=null&poisk%5Btransmission%5D=1&poisk%5Bmodel_id%5D=null&poisk%5Bprice_from%5D=0&poisk%5Bprice_to%5"
				+ "D=650000&poisk%5Bcity_id%5D=null&poisk%5Bdrive%5D=null&poisk%5Bbody_type%5D=null&poisk%5Bcentre_id%5D=null&poisk%"
				+ "5Bengine_power%5D=null&poisk%5Bengine_type%5D=null&poisk%5Bmileage%5D=null&new=&page=1";
		Document doc = Jsoup.connect(adress).get();
		Elements carsDiv = doc.body().getElementsByClass("col-lg-3 col-md-4 col-sm-6");

		for (Element carDiv : carsDiv) {
			
			StringBuilder rref = new StringBuilder(carDiv.getElementsByAttribute("href").first().attr("href"));
			rref.reverse();
			
			String strRref = "";
			Matcher m = Pattern.compile("/.+?/").matcher(rref);
			if (m.find()) {
				rref = new StringBuilder(m.group());
				rref.reverse();
				
				m = Pattern.compile("/").matcher(rref.toString());
				strRref = m.replaceAll("");
				
			} else {
				continue;
			}
			
					
			URL url = new URL(
					"https://photos.okami-market.ru/images/auto/extra/"+strRref+"-exterior-front.jpg");

			BufferedImage inputImage = ImageIO.read(url.openStream());
			ByteArrayOutputStream jpgContent = new ByteArrayOutputStream();
			byte[] picInBytes;
			try {
				ImageIO.write(inputImage, "jpg", jpgContent);
				picInBytes = jpgContent.toByteArray();
			} catch (IllegalArgumentException e) {
				picInBytes = null;
			}		

			CarAdvert advert = new CarAdvert();
			advert.setEngineCapacity(1.4f);
			advert.setImage(picInBytes);
			advert.setProductionYear(2014);
			advert.setModel(modelRepo.findById(1l));

			advertRepo.save(advert, false);
		}

		// Ссылка /avtomobili/mitsubishi-lancer-11744/
		// carDiv.getElementsByAttribute("href").first().attr("href")

		// Картинки
		// https://photos.okami-market.ru/images/auto/extra/hyundai-solaris-11720-exterior-front.jpg
		// https://photos.okami-market.ru/images/auto/large/hyundai-solaris-11720-exterior-front.jpg
		// https://photos.okami-market.ru/integration/resize/?auto=11720&width=300&height=206&angle=exterior-front&brand=hyundai&model=solaris

		// Год выпуска Hyundai Solaris 2015
		// carDiv.getElementsByClass("auto-name").first().html()

		// Объем пробег
		// carDiv.getElementsByClass("auto-short-description").first().html()

		// Цена
		// carDiv.getElementsByClass("price").first().html()

//		Matcher m = Pattern.compile("\\w+")
//				.matcher("Evening is full of the linnet’s wings");
//				while(m.find())
//				printnb(m.group() + " ");

		return "parsing";
	}
}
