package itech.s4sgen.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import itech.s4sgen.dao.OrderPaymentDao;
import itech.s4sgen.dao.OrdersDao;
import itech.s4sgen.dto.OrderDto;
import itech.s4sgen.models.Order;
import itech.s4sgen.models.OrderPayment;
import itech.s4sgen.models.User;

@Service
public class OrdersService {

	@Autowired
	OrdersDao ordersDao;
	
	@Autowired
	OrderPaymentDao orderPaymentDao;
	
	@Autowired
	UserService userService;
	
	public boolean verifyUserAccess(String userName, int id) {
		User user = userService.getUserByLogin(userName);
		String featureString = user.getFeatures();
		if(featureString.contains(String.valueOf(id)))
			return true;
		
		return false;
	}
	
	public void saveOrder(OrderDto dto, String userName) {
		User user = userService.getUserByLogin(userName);
		Order order;
		if(dto.getId()==0)
			order = new Order();
		else
			order = ordersDao.findOne(dto.getId());
		order.setCustomerName(dto.getCustomerName());
		order.setCustomerType(dto.getCustomerType());
		order.setOrderedDish(dto.getOrderedDish());
		order.setOrderType(dto.getOrderType());
		order.setDeliveryTime(dto.getDeliveryTime());
		order.setOrderBill(dto.getOrderBill());
		order.setUser(user);
		ordersDao.save(order);
	}

	public void saveOrderPayment(OrderDto dto, String userName) {
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
	
	public List<OrderDto> getUserOrdersByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return ordersDao.findAllByUser(user).stream().map(OrdersService::convertToOrderDtoFromOrder).collect(Collectors.toList());
	}
	
	public List<OrderDto> getUserOrderPaymentsByUserName(String userName){
		User user = userService.getUserByLogin(userName);
		return orderPaymentDao.findAllByUser(user).stream().map(OrdersService::convertToOrderDtoFromOrderPayment).collect(Collectors.toList());
	}
	
	public List<OrderDto> getUserOrdersReviews(String userName){
		User user = userService.getUserByLogin(userName);
		List<Order> orders = ordersDao.findAllByUser(user);
		List<OrderDto> dtoList = new ArrayList<>();
		for(Order o: orders) {
			OrderDto dto = new OrderDto();
			dto.setCustomerName(o.getCustomerName());
			dto.setOrderedDish(o.getOrderedDish());
			dto.setOrderType(o.getOrderType());
			dto.setReview(orderPaymentDao.findOneByOrderId((int)o.getId()).getReview());
			dtoList.add(dto);
		}
		return dtoList;
	}
	
	public OrderDto getOrderById(long id) {
		return convertToOrderDtoFromOrder(ordersDao.findOne(id));
	}
	
	public OrderDto getOrderPaymentById(long id) {
		return convertToOrderDtoFromOrderPayment(orderPaymentDao.findOne(id));
	}
	
	public File generateReport(String userName) throws FileNotFoundException, DocumentException {
		User user = userService.getUserByLogin(userName);
		List<Order> orders = ordersDao.findAllByUser(user);
		System.out.println("orders " + orders.size());
		
		File pdf = new File("C:/temp/"+userName+".pdf");
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(pdf));
		doc.open();
		doc.addTitle(userName+ " Orders Report");
		doc.addAuthor("s4sgen");
		doc.add(new Paragraph(userName.toUpperCase() + " Orders Report"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("\n"));
		doc.add(new Paragraph("=== Orders Report ==="));
		for(Order sa : orders) {
			doc.add(new Paragraph(sa.getOrderedDish() + " :\t " + sa.getOrderType() + " :\t " + sa.getOrderBill()));
			doc.add(new Paragraph("Review \t:\t " + orderPaymentDao.findOneByOrderId((int)sa.getId()).getReview()));
		}
		doc.add(new Paragraph("\n"));
		
		
		doc.close();
		writer.close();
		
		return pdf;
	}
	
	public static OrderDto convertToOrderDtoFromOrder(Order order) {
		OrderDto dto = new OrderDto();
		dto.setId(order.getId());
		dto.setCustomerName(order.getCustomerName());
		dto.setCustomerType(order.getCustomerType());
		dto.setOrderedDish(order.getOrderedDish());
		dto.setOrderType(order.getOrderType());
		dto.setDeliveryTime(order.getDeliveryTime());
		dto.setOrderBill(order.getOrderBill());
		return dto;
	}
	
	public static OrderDto convertToOrderDtoFromOrderPayment(OrderPayment payment) {
		OrderDto dto = new OrderDto();
		dto.setId(payment.getId());
		dto.setOrderId(payment.getOrderId());
		dto.setReview(payment.getReview());
		dto.setAmountReceived(payment.getAmountReceived());
		dto.setAmountDeducted(payment.getAmountDeducted());
		return dto;
	}
	
}
