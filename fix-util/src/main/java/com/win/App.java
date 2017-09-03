package com.win;

import com.win.slots.protocol.event.sale.SaleEventType;
import com.win.service.FixSaleEventsService;
import com.win.util.Cli;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static com.win.util.OutUtil.saleEventType;

@ComponentScan("com.win.*")
@SpringBootApplication
@EnableJpaRepositories
@EnableMongoRepositories
public class App
		extends SpringBootServletInitializer
{

	public static void main(String[] args)
			throws Exception
	{
		Cli cli = new Cli(args);
		cli.actions();

		ApplicationContext ctx = SpringApplication.run(App.class, args);

		FixSaleEventsService fixSaleEventsService = ctx.getBean(FixSaleEventsService.class);

		// filter by 'Mini Game Popup'
		SaleEventType filter = (cli.getFilter()==null) ?
				null :
				SaleEventType.valueOf(cli.getFilter().toString());
		fixSaleEventsService.run(filter);
	}

	@Override
	public void onStartup(ServletContext servletContext)
			throws ServletException
	{
		servletContext.setInitParameter("com.sun.faces.expressionFactory", "org.apache.el.ExpressionFactoryImpl");
	}

}
