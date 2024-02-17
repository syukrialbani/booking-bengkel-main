package com.bengkel.booking.services;

import java.util.List;
import java.util.Scanner;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.repositories.CustomerRepository;

public class MenuService {
	private static List<Customer> listAllCustomers = CustomerRepository.getAllCustomer();
	private static Scanner input = new Scanner(System.in);
	public static void run() {
		boolean isLooping = true;
		do {
			Customer loggedInCustomer = login();
			if (loggedInCustomer != null) {
                mainMenu(loggedInCustomer);
            } else {
                System.out.println("Login failed. Please try again.");
            }
		} while (isLooping);
		
	}
	
	public static Customer login() {
        System.out.print("Enter Customer ID: ");
        String customerId = input.nextLine();
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        return listAllCustomers.stream()
                .filter(customer -> customer.getCustomerId().equals(customerId) && customer.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }
	
	public static void mainMenu(Customer customer) {
		String[] listMenu = {"Informasi Customer", "Booking Bengkel", "Top Up Bengkel Coin", "Informasi Booking", "Logout"};
		int menuChoice = 0;
		boolean isLooping = true;
		
		do {
			PrintService.printMenu(listMenu, "Booking Bengkel Menu");
			menuChoice = Validation.validasiNumberWithRange("Masukan Pilihan Menu:", "Input Harus Berupa Angka!", "^[0-9]+$", listMenu.length-1, 0);
			System.out.println(menuChoice);
			
			switch (menuChoice) {
			case 1:
				BengkelService.displayCustomerProfile(customer);
				break;
			case 2:
				BengkelService.bookService(customer);
				break;
			case 3:
				BengkelService.topUpSaldoCoin(customer);
				break;
			case 4:
				BengkelService.informasiBookingOrder();
				break;
			default:
				System.out.println("Logout");
				isLooping = false;
				break;
			}
		} while (isLooping);
		
		
	}
	
	
}
