package com.bengkel.booking.services;

import java.util.List;
import java.util.stream.Collectors;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Car;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.Vehicle;

public class PrintService {
	
	public static void printMenu(String[] listMenu, String title) {
		String line = "+---------------------------------+";
		int number = 1;
		String formatTable = " %-2s. %-25s %n";
		
		System.out.printf("%-25s %n", title);
		System.out.println(line);
		
		for (String data : listMenu) {
			if (number < listMenu.length) {
				System.out.printf(formatTable, number, data);
			}else {
				System.out.printf(formatTable, 0, data);
			}
			number++;
		}
		System.out.println(line);
		System.out.println();
	}
	
	public static void printVechicle(List<Vehicle> listVehicle) {
		String formatTable = "| %-2s | %-15s | %-10s | %-15s | %-15s | %-5s | %-15s |%n";
		String line = "+----+-----------------+------------+-----------------+-----------------+-------+-----------------+%n";
		System.out.format(line);
	    System.out.format(formatTable, "No", "Vechicle Id", "Warna", "Brand", "Transmisi", "Tahun", "Tipe Kendaraan");
	    System.out.format(line);
	    int number = 1;
	    String vehicleType = "";
	    for (Vehicle vehicle : listVehicle) {
	    	if (vehicle instanceof Car) {
				vehicleType = "Mobil";
			}else {
				vehicleType = "Motor";
			}
	    	System.out.format(formatTable, number, vehicle.getVehiclesId(), vehicle.getColor(), vehicle.getBrand(), vehicle.getTransmisionType(), vehicle.getYearRelease(), vehicleType);
	    	number++;
	    }
	    System.out.printf(line);
	}
	
	//Silahkan Tambahkan function print sesuai dengan kebutuhan.
	public static void printServiceItems(List<ItemService> itemServices) {
        String format = "| %-8s | %-20s | %-10s | %-15s |%n";
        System.out.format("+----------+----------------------+------------+-----------------+%n");
        System.out.format("| ServiceId| Service Name         | Vehicle    | Price           |%n");
        System.out.format("+----------+----------------------+------------+-----------------+%n");
        for (ItemService itemService : itemServices) {
            System.out.format(format, itemService.getServiceId(), itemService.getServiceName(), itemService.getVehicleType(), itemService.getPrice());
        }
        System.out.format("+----------+----------------------+------------+-----------------+%n");
    }

	public static void printBookingOrders(List<BookingOrder> bookingOrders) {
		String formatTable = "| %-2s | %-20s | %-20s | %-15s | %-15s | %-15s | %-20s |%n";
		String line = "+----+---------------------+----------------------+-----------------+-----------------+-----------------+----------------------+%n";
		System.out.format(line);
		System.out.format(formatTable, "No", "Booking Id", "Nama Customer", "Payment Method", "Total Service", "Total Payment", "List Service");
		System.out.format(line);
		for (int i = 0; i < bookingOrders.size(); i++) {
			BookingOrder bookingOrder = bookingOrders.get(i);
			String serviceNames = bookingOrder.getServices().stream().map(ItemService::getServiceName).collect(Collectors.joining(", "));
			System.out.format(formatTable, (i + 1), bookingOrder.getBookingId(), bookingOrder.getCustomer().getName(), 
							bookingOrder.getPaymentMethod(), bookingOrder.getTotalServicePrice(), bookingOrder.getTotalPayment(), serviceNames);
		}
		System.out.printf(line);
	}


	
}
