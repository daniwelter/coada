package uk.ac.ebi.fgpt.coada;


import org.springframework.context.*;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.ebi.fgpt.coada.service.ClassRetriever;


public class ExtractorDriver {


    public static void main(String args[]){
        ExtractorDriver driver = new ExtractorDriver();
        driver.extract();

    }

    private ClassRetriever retriever;

    public ExtractorDriver(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("classextractor.xml");
        retriever = ctx.getBean("retriever", ClassRetriever.class);

    }

    public void extract(){
        retriever.dispatchAnnotator();
        System.out.println("All annotations retrieved");
    }

}
