package ru.planetavto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class Application 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(Application.class, args);
    }
    
    //Document doc = Jsoup.connect("https://okami-market.ru/poisk-more/?poisk%5Bbrand_id%5D=null&poisk%5Byear_from%5D=null&poisk%5Byear_to%5D=null&poisk%5Btransmission%5D=1&poisk%5Bmodel_id%5D=null&poisk%5Bprice_from%5D=0&poisk%5Bprice_to%5D=650000&poisk%5Bcity_id%5D=null&poisk%5Bdrive%5D=null&poisk%5Bbody_type%5D=null&poisk%5Bcentre_id%5D=null&poisk%5Bengine_power%5D=null&poisk%5Bengine_type%5D=null&poisk%5Bmileage%5D=null&new=&page=1").get();
	//Elements carDiv = doc.body().getElementsByClass("col-lg-3 col-md-4 col-sm-6");
}
