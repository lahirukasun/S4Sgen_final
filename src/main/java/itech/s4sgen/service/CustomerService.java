package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.CustomerDao;
import itech.s4sgen.dao.OrderPaymentDao;
import itech.s4sgen.dao.RegCustomerOrderDao;
import itech.s4sgen.dto.CustomerDto;
import itech.s4sgen.models.Customer;
import itech.s4sgen.models.OrderPayment;
import itech.s4sgen.models.RegCustomerOrder;
import itech.s4sgen.models.User;
import itech.s4sgen.utils.HelpingClass;

@Service
public class CustomerService {

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private RegCustomerOrderDao regCustomerOrderDao;
	
	@Autowired
	private OrderPaymentDao orderPaymentDao;
	
	@Autowired
	private UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveCustomer(CustomerDto dto, String userName) throws ParseException {
		User user = userService.getUserByLogin(userName);
		Customer customer;
		if(dto.getId()==0)
			customer = new Customer();
		else
			customer = customerDao.findOne(dto.getId());
		customer.setName(dto.getName());
		customer.setMobile(dto.getMobile());
		customer.setMembershipId(dto.getMembershipId());
		customer.setMembershipType(dto.getMembershipType());
		customer.setJoiningDate(HelpingClass.stringToDate(dto.getJoinDate()));
		customer.setUser(user);
		customerDao.save(customer);
	}
	
	public void saveRegCustomerOrder(CustomerDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		RegCustomerOrder order;
		if(dto.getId()==0)
			order = new RegCustomerOrder();
		else
			order = regCustomerOrderDao.findOne(dto.getId());
		order.setCustomerId(dto.getCustomerId());
		order.setOrderedDish(dto.getOrderedDish());
		order.setOrderType(dto.getOrderType());
		order.setDeliveryTime(dto.getDeliveryTime());
		order.setOrderBill(dto.getOrderBill());
		order.setCustomer(customerDao.findOne((long)dto.getCustomerId()));
		order.setUser(user);
		regCustomerOrderDao.save(order);
	}
	
	public void saveOrderPayment(CustomerDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		OrderPayment payment;
		if(dto.getId()==0)
			payment = new OrderPayment();
		else
			payment = orderPaymentDao.findOne(dto.getId());
		payment.setOrderId(dto.getOrderId());
		payment.setReview(dto.getReview());
		payment.setAmountReceived(dto.getAmountReceived());
		payment.setAmountDeducted(dto.getAmountDeducted());
		payment.setUser(user);
		orderPaymentDao.save(payment);
	}
	
	public List<CustomerDto> getUserCustomersByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return customerDao.findAllByUser(user).stream().map(CustomerService::convertToCustomerDtoFromCustomer).collect(Collectors.toList());
	}
	
	public List<CustomerDto> getUserCustomerOrdersByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return regCustomerOrderDao.findAllByUser(user).stream().map(CustomerService::convertToCustomerDtoFromRegCustomerOrder).collect(Collectors.toList());
	}
	
	public List<CustomerDto> getUserOrderPaymentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return orderPaymentDao.findAllByUser(user).stream().map(CustomerService::convertToCustomerDtoFromOrderPayment).collect(Collectors.toList());
	}
	
	public CustomerDto getCustomerDtoById(long id) {
		return convertToCustomerDtoFromCustomer(customerDao.findOne(id));
	}
	
	public CustomerDto getRegCustomerOrderById(long id) {
		return convertToCustomerDtoFromRegCustomerOrder(regCustomerOrderDao.findOne(id));
	}
	
	public CustomerDto getOrderPaymentById(long id) {
		return convertToCustomerDtoFromOrderPayment(orderPaymentDao.findOne(id));
	}
	
	public List<CustomerDto> getUserCustomersDataByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		List<Customer> customers = customerDao.findAllByUser(user);
		List<CustomerDto> dtoList = new ArrayList<>();
		for(Customer c : customers) {
			List<RegCustomerOrder> orders = regCustomerOrderDao.findAllByCustomer(c);
			CustomerDto dto = new CustomerDto();
			dto.setId(c.getId());
			dto.setName(c.getName());
			dto.setJoinDate(c.getJoiningDate()+"");
			dto.setOrderId(orders.size());
			dto.setOrderBill(orders.stream().mapToDouble(o->o.getOrderBill()).sum());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public File generateReport(long id) throws FileNotFoundException, DocumentException {
		Customer st = customerDao.findOne(id);
		List<RegCustomerOrder> orders = regCustomerOrderDao.findAllByCustomer(st);
		System.out.println("attendance " + orders.size());
		
		File pdf = new File("C:/temp/"+st.getId()+" " + st.getName() +".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(st.getName()+ " Customer Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(st.getName().toUpperCase() + " " + st.getId() + " Doctor Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("Mobile: \t" + st.getMobile()));
		doc.add(new Paragraph("Membership Id: \t" + st.getMembershipId()));
		doc.add(new Paragraph("Membership Type: \t" + st.getMembershipType()));
		doc.add(new Paragraph("=== Orders Report ==="));
		for(RegCustomerOrder sa : orders) {
			doc.add(new Paragraph(sa.getOrderedDish() + " :\t " + sa.getOrderType() + " :\t " + sa.getOrderBill()));
		}
		doc.add(new Paragraph("\n"));
		
		
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static CustomerDto convertToCustomerDtoFromCustomer(Customer customer) {
		CustomerDto dto = new CustomerDto();
		dto.setId(customer.getId());
		dto.setName(customer.getName());
		dto.setMobile(customer.getMobile());
		dto.setMembershipId(customer.getMembershipId());
		dto.setMembershipType(customer.getMembershipType());
		dto.setJoinDate(customer.getJoiningDate()+"");
		return dto;
	}
	
	public static CustomerDto convertToCustomerDtoFromRegCustomerOrder(RegCustomerOrder order) {
		CustomerDto dto = new CustomerDto();
		dto.setId(order.getId());
		dto.setCustomerId(order.getCustomerId());
		dto.setName(order.getCustomer().getName());
		dto.setOrderedDish(order.getOrderedDish());
		dto.setOrderType(order.getOrderType());
		dto.setDeliveryTime(order.getDeliveryTime());
		dto.setOrderBill(order.getOrderBill());
		return dto;
	}
	
	public static CustomerDto convertToCustomerDtoFromOrderPayment(OrderPayment payment) {
		CustomerDto dto = new CustomerDto();
		dto.setId(payment.getId());
		dto.setOrderId(payment.getOrderId());
		dto.setReview(payment.getReview());
		dto.setAmountReceived(payment.getAmountReceived());
		dto.setAmountDeducted(payment.getAmountDeducted());
		return dto;
	}
}
