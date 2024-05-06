package com.app.PortScanner;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@SpringBootApplication
@RestController
public class PortScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortScannerApplication.class, args);
	}

	@GetMapping("/")
	public String index(HttpServletRequest request) throws InterruptedException {
		System.out.println("index");
		return request.getRemoteAddr();
	}

	@GetMapping("/test")
	public String portScan(HttpServletRequest request, @RequestParam("port") Optional<String> portParam) throws InterruptedException {
		String port = portParam.orElse("");
		if (port.equals("")) return portScanAll(request);
		PortScannerThread scan = new PortScannerThread(request.getRemoteAddr(), port);
		scan.start();
		while(scan.getStatus().equals("UNKNOWN")) Thread.sleep(100);
		return scan.getStatus();
	}

	private String portScanAll(HttpServletRequest request) throws InterruptedException {
		if (PortScanner.IN_PROGRESS) {
			System.out.println("Rejected");
			return "Another scan is in progress, please try again later";
		}
		PortScanner scan = new PortScanner(request.getRemoteAddr());
		scan.run();
		return scan.getInterestingPorts();
	}
}
