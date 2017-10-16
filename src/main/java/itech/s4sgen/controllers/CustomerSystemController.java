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

import itech.s4sgen.dto.CustomerDto;
import itech.s4sgen.service.CustomerService;
import itech.s4sgen.utils.HelpingClass;

@Controller
public class CustomerSystemController {

	@Autowired
	CustomerService customerService;
	
	@RequestMapping(value= {"/add_manage_customer/{id}",
			"/make_customer_order/{id}",
			"/make_order_payment/{id}",
			"generate_customer_report/{id}"} , method = RequestMethod.GET)
	public String addManageCustomers(@PathVariable("id")int id, Model model) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean access = customerService.verifyUserAccess(userName, id);
		if(access) {
			switch(id) {
			case 1:{
				model.addAttribute("customer", new CustomerDto());
				model.addAttribute("types", HelpingClass.getMemberShipTypeList());
				model.addAttribute("customers", customerService.getUserCustomersByUserName(userName));
				return "customersystem/customermanagementsystem";
			}
			case 2:{
				model.addAttribute("order", new CustomerDto());
				model.addAttribute("orders", customerService.getUserCustomerOrdersByUserName(userName));
				return "customersystem/ordersystem";
			}
			case 3:{
				model.addAttribute("payment", new CustomerDto());
				model.addAttribute("payments", customerService.getUserOrderPaymentsByUserName(userName));
				return "customersystem/paymentsystem";
			}
			case 4:{
				model.addAttribute("customerData", customerService.getUserCustomersDataByUserName(userName));
				return "customersystem/generatecustomerreport";
			}
			}
		}
		return "accessdenied";
	}
	
	@RequestMapping(value="/add_customer", method=RequestMethod.POST)
	public String addCustomer(@ModelAttribute CustomerDto customer, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		customerService.saveCustomer(customer, userName);
		model.addAttribute("customer", new CustomerDto());
		model.addAttribute("types", HelpingClass.getMemberShipTypeList());
		model.addAttribute("customers", customerService.getUserCustomersByUserName(userName));
		return "customersystem/customermanagementsystem";
	}
	
	@RequestMapping(value="/add_order", method=RequestMethod.POST)
	public String addOrder(@ModelAttribute CustomerDto order, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		customerService.saveRegCustomerOrder(order, userName);
		model.addAttribute("order", new CustomerDto());
		model.addAttribute("orders", customerService.getUserCustomerOrdersByUserName(userName));
		return "customersystem/ordersystem";
	}
	
	@RequestMapping(value="/add_payment", method=RequestMethod.POST)
	public String addPayment(@ModelAttribute CustomerDto payment, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		customerService.saveOrderPayment(payment, userName);
		model.addAttribute("payment", new CustomerDto());
		model.addAttribute("payments", customerService.getUserOrderPaymentsByUserName(userName));
		return "customersystem/paymentsystem";
	}
	
	@RequestMapping(value="/edit_customer/{id}", method=RequestMethod.GET)
	public String editCustomer(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("customer", customerService.getCustomerDtoById(id));
		model.addAttribute("types", HelpingClass.getMemberShipTypeList());
		model.addAttribute("customers", customerService.getUserCustomersByUserName(userName));
		return "customersystem/customermanagementsystem";
	}
	
	@RequestMapping(value="/edit_order/{id}", method=RequestMethod.GET)
	public String editOrder(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("order", customerService.getRegCustomerOrderById(id));
		model.addAttribute("orders", customerService.getUserCustomerOrdersByUserName(userName));
		return "customersystem/ordersystem";
	}
	
	@RequestMapping(value="/edit_payment/{id}", method=RequestMethod.GET)
	public String editPayment(@PathVariable("id")long id, Model model) throws ParseException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		
		model.addAttribute("payment", customerService.getOrderPaymentById(id));
		model.addAttribute("payments", customerService.getUserOrderPaymentsByUserName(userName));
		return "customersystem/paymentsystem";
	}

	@RequestMapping(value="/download_customer_report/{id}",method = RequestMethod.GET)
	public void generateDoctorReport(@PathVariable("id") long id, HttpServletResponse response) {
		
		File report = null;
		try {
			report = customerService.generateReport(id);
			response.reset();
			response.setBufferSize(1024);
			response.setContentType("application/pdf");
			response.getOutputStream().write(Files.readAllBytes(Paths.get(report.getAbsolutePath())));
			
		}catch(Exception ex) {
			System.out.println("Exception found is: " + ex);
		}
	}
	
}
