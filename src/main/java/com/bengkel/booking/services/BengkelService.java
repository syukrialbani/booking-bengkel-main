package com.bengkel.booking.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.bengkel.booking.models.BookingOrder;
import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;
import com.bengkel.booking.repositories.ItemServiceRepository;

public class BengkelService {
    private static List<ItemService> listAllItemService = ItemServiceRepository.getAllItemService();
    private static List<BookingOrder> bookingOrders = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);
	private static int lastBookingNumber = 0;

	
    //Info Customer
    public static void displayCustomerProfile(Customer customer) {
        if (customer != null) {
            System.out.println("Customer Id: " + customer.getCustomerId());
            System.out.println("Nama: " + customer.getName());
            System.out.println("Customer Status: " + (customer instanceof MemberCustomer ? "Member" : "Non Member"));
            System.out.println("Alamat: " + customer.getAddress());
            if (customer instanceof MemberCustomer) {
                System.out.println("Saldo Koin: " + ((MemberCustomer) customer).getSaldoCoin());
            }
            System.out.println("List Kendaraan:");
            PrintService.printVechicle(customer.getVehicles());
        } else {
            System.out.println("Customer tidak ditemukan atau password salah!");
        }
    }
    
    //Booking atau Reservation
    public static void bookService(Customer customer) {
        System.out.println("List Kendaraan:");
        PrintService.printVechicle(customer.getVehicles());

        System.out.print("Masukkan Vehicle Id kendaraan yang akan diperbaiki: ");
        String selectedVehicleId = input.nextLine();

        Vehicle selectedVehicle = customer.getVehicles().stream()
                .filter(vehicle -> vehicle.getVehiclesId().equals(selectedVehicleId))
                .findFirst()
                .orElse(null);

        if (selectedVehicle == null) {
            System.out.println("Kendaraan tidak ditemukan.");
            return;
        }

        System.out.println("List Service yang Tersedia untuk kendaraan " + selectedVehicle.getVehicleType() + ":");
        List<ItemService> availableServices = listAllItemService.stream()
                .filter(itemService -> itemService.getVehicleType().equalsIgnoreCase(selectedVehicle.getVehicleType()))
                .collect(Collectors.toList());
        PrintService.printServiceItems(availableServices);

        List<ItemService> selectedServices = new ArrayList<>();
        String choice = "";
        do {
            System.out.print("Silahkan masukkan Service Id: ");
            String serviceId = input.nextLine();

            ItemService selectedService = availableServices.stream()
                    .filter(itemService -> itemService.getServiceId().equalsIgnoreCase(serviceId))
                    .findFirst()
                    .orElse(null);

            if (selectedService == null) {
                System.out.println("Service Id tidak valid.");
                continue;
            }

            selectedServices.add(selectedService);

            System.out.print("Apakah Anda ingin menambahkan Service Lainnya? (y/t): ");
            choice = input.nextLine();
        } while (choice.equalsIgnoreCase("y"));

        String paymentMethod;
        if (customer instanceof MemberCustomer) {
            System.out.print("Silahkan Pilih Metode Pembayaran (Saldo Coin atau Cash): ");
            paymentMethod = input.nextLine();
            if (paymentMethod.equalsIgnoreCase("Saldo Coin")) {
                double saldoCoin = ((MemberCustomer) customer).getSaldoCoin();
                double totalServicePrice = selectedServices.stream().mapToDouble(ItemService::getPrice).sum();
                if (saldoCoin < totalServicePrice) {
                    System.out.println("Saldo koin tidak mencukupi.");
                    return;
                }
                ((MemberCustomer) customer).setSaldoCoin(saldoCoin - totalServicePrice);
            }
        } else {
            paymentMethod = "Cash";
        }

        double totalServicePrice = selectedServices.stream().mapToDouble(ItemService::getPrice).sum();

        System.out.println("\nBooking Berhasil!!!");
        System.out.println("Total Harga Service: " + totalServicePrice);

        double totalPayment;
        if (paymentMethod.equalsIgnoreCase("Saldo Coin")) {
            totalPayment = totalServicePrice * 0.9;
        } else {
            totalPayment = totalServicePrice;
        }
        System.out.println("Total Pembayaran: " + totalPayment);

        System.out.print("\nApakah Anda yakin ingin melakukan booking ini? (y/t): ");
        String confirmation = input.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
			String bookingId = generateBookingId(customer.getCustomerId(), selectedServices);

            BookingOrder bookingOrder = new BookingOrder();
			bookingOrder.setBookingId(bookingId);
			bookingOrder.setCustomer(customer);
			bookingOrder.setServices(selectedServices);
			bookingOrder.setPaymentMethod(paymentMethod);
			bookingOrder.setTotalServicePrice(totalServicePrice);
			bookingOrder.setTotalPayment(totalPayment);
			bookingOrders.add(bookingOrder);
            System.out.println("Booking berhasil dilakukan.");
        } else {
            System.out.println("Booking dibatalkan.");
        }
    }

	private static String generateBookingId(String customerId, List<ItemService> selectedServices) {
		lastBookingNumber++;

		String bookingId = "Book-" + "Cust" + "-" + String.format("%03d", lastBookingNumber) + customerId.substring(customerId.length() - 3);
        return bookingId;
	}
    
    // Getter statis untuk daftar pesanan booking
    public static List<BookingOrder> getBookingOrders() {
        return bookingOrders;
    }

    // Top Up Saldo Coin Untuk Member Customer
    public static void topUpSaldoCoin(Customer customer) {
        if (customer instanceof MemberCustomer) {
            double topUpAmount = Double.parseDouble(
				Validation.validasiInput("Masukkan besaran Top Up: ", 
										"Jumlah saldo yang dimasukkan tidak valid.", 
										"\\d+(\\.\\d+)?"));

                MemberCustomer memberCustomer = (MemberCustomer) customer;
                double currentBalance = memberCustomer.getSaldoCoin();
                memberCustomer.setSaldoCoin(currentBalance + topUpAmount);
                System.out.println("Top Up berhasil! Saldo koin Anda sekarang: " + memberCustomer.getSaldoCoin());
            
        } else {
            System.out.println("Maaf, menu ini hanya berlaku untuk member.");
        }
    }

    public static void informasiBookingOrder() {
        if (bookingOrders.isEmpty()) {
            System.out.println("Tidak ada booking order yang tersedia.");
        } else {
            System.out.println("Booking Order Menu\n");
            PrintService.printBookingOrders(bookingOrders);
        }
    }
}
