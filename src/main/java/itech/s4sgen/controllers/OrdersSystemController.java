package itech.s4sgen.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import itech.s4sgen.dto.OrderDto;
import itech.s4sgen.service.OrdersService;

@Controller
public class OrdersSystemController {
	
	
	@Autowired
	private OrdersService ordersService;

	@RequestMapping(value= {"/add_new_order/{id}" , 
			"/add_order_payment/{id}",
			"/view_orders_reviews/{id}",
			"/generate_orders_report/{id}"} , method= RequestMethod.GET)
	public String addManageOrdersSystem(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		if(ordersService.verifyUserAccess(userName, id)) {
			switch(id) {
			case 1:{
				model.addAttribute("order",new OrderDto());
				model.addAttribute("ordersList",ordersService.getUserOrdersByUserName(userName));
				return "ordersystem/ordersystem";
			}
			case 2:{
				model.addAttribute("payment",new OrderDto());
				model.addAttribute("paymentsList",ordersService.getUserOrderPaymentsByUserName(userName));
				return "ordersystem/paymentsystem";
			}
			case 3:{
				model.addAttribute("reviewList",ordersService.getUserOrdersReviews(userName));
				return "ordersystem/ordersreviews";
			}
			case 4:{
				return "ordersystem/generateorderreport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_dish_order", method=RequestMethod.POST)
	public String addOrder(@ModelAttribute OrderDto order, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		ordersService.saveOrder(order, userName);
		model.addAttribute("order",new OrderDto());
		model.addAttribute("ordersList",ordersService.getUserOrdersByUserName(userName));
		return "ordersystem/ordersystem";
	}
	
	@RequestMapping(value="/add_dish_order_payment", method=RequestMethod.POST)
	public String addPayment(@ModelAttribute OrderDto payment, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		ordersService.saveOrderPayment(payment, userName);
		model.addAttribute("payment",new OrderDto());
		model.addAttribute("paymentsList",ordersService.getUserOrderPaymentsByUserName(userName));
		return "ordersystem/paymentsystem";
	}
	
	@RequestMapping(value="/edit_dish_order/{id}", method=RequestMethod.GET)
	public String editOrder(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("order",ordersService.getOrderById(id));
		model.addAttribute("ordersList",ordersService.getUserOrdersByUserName(userName));
		return "ordersystem/ordersystem";
	}
	
	@RequestMapping(value="/edit_dish_order_payment/{id}", method=RequestMethod.GET)
	public String editPayment(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("payment",ordersService.getOrderPaymentById(id));
		model.addAttribute("paymentsList",ordersService.getUserOrderPaymentsByUserName(userName));
		return "ordersystem/paymentsystem";
	}

	@RequestMapping(value="/download_orders_report",method = RequestMethod.GET)
	public void generateDoctorReport( HttpServletResponse response) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		File report = null;
		try {
			report = ordersService.generateReport(userName);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
}
