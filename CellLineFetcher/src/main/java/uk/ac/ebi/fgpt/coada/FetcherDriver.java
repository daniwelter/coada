package uk.ac.ebi.fgpt.coada;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.fgpt.coada.service.SearchDispatcherService;

/**
 * Created by Dani on 13/06/2014.
 */
public class FetcherDriver {

    public static void main(String args[]){
        FetcherDriver driver = new FetcherDriver();
        driver.extract();

    }

    private SearchDispatcherService dispatcherService;

    public FetcherDriver(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("cell-linefetcher.xml");
        dispatcherService = ctx.getBean("dispatcherService", SearchDispatcherService.class);

    }

    public void extract(){
        dispatcherService.findCellLines();
        System.out.println("All cell lines retrieved");
    }
}
