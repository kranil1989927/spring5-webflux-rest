package learn.springframework.controllers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;

import learn.springframework.domain.Category;
import learn.springframework.domain.Vendor;
import learn.springframework.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VendorControllerTest {

	WebTestClient webTestClient;
	VendorController vendorController;
	VendorRepository vendorRepository;

	@Before
	public void setUp() throws Exception {
		vendorRepository = Mockito.mock(VendorRepository.class);
		vendorController = new VendorController(vendorRepository);
		webTestClient = WebTestClient.bindToController(vendorController).build();
	}

	@Test
	public void list() {
		BDDMockito.given(vendorRepository.findAll())
			.willReturn(Flux.just(new Vendor("A", "B"), new Vendor("C", "D")));

		webTestClient.get()
			.uri("/api/v1/vendors/")
			.exchange()
			.expectBodyList(Vendor.class)
			.hasSize(2);
	}

	@Test
	public void getById() {
		BDDMockito.given(vendorRepository.findById("someId"))
			.willReturn(Mono.just(new Vendor("A", "B")));

		webTestClient.get()
			.uri("/api/v1/vendors/someId")
			.exchange()
			.expectBody(Category.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test 
	public void create(){
		BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
			.willReturn(Flux.just(new Vendor("A", "B")));
		
		Mono<Vendor> venToSaveMono = Mono.just(new Vendor("A", "B"));
		
		webTestClient.post()
			.uri("/api/v1/vendors")
			.body(venToSaveMono, Vendor.class)
			.exchange()
			.expectStatus()
			.isCreated();
	}

	@Test
	public void update(){
		
		BDDMockito.given(vendorRepository.save(any(Vendor.class)))
			.willReturn(Mono.just(new Vendor("A", "B")));
		
		Mono<Vendor> venToupdateMono = Mono.just(new Vendor("A", "B"));
		
		webTestClient.put()
			.uri("/api/v1/vendors/someid")
			.body(venToupdateMono, Vendor.class)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	@Test
	public void patchWithChanges(){
		
		BDDMockito.given(vendorRepository.findById(anyString()))
		.willReturn(Mono.just(new Vendor("A", "B")));
		
		BDDMockito.given(vendorRepository.save(any(Vendor.class)))
			.willReturn(Mono.just(new Vendor("A", "B")));
		
		Mono<Vendor> venToupdateMono = Mono.just(new Vendor("A1", "B"));
		
		webTestClient.patch()
			.uri("/api/v1/vendors/someid")
			.body(venToupdateMono, Vendor.class)
			.exchange()
			.expectStatus()
			.isOk();
		
		BDDMockito.verify(vendorRepository).save(any());
	}
	
	@Test
	public void patchWithNoChanges(){

		BDDMockito.given(vendorRepository.findById(anyString()))
			.willReturn(Mono.just(new Vendor("A", "B")));
		
		BDDMockito.given(vendorRepository.save(any(Vendor.class)))
			.willReturn(Mono.just(new Vendor("A", "B")));
		
		Mono<Vendor> venToupdateMono = Mono.just(new Vendor("A", "B"));
		
		webTestClient.patch()
			.uri("/api/v1/vendors/someid")
			.body(venToupdateMono, Vendor.class)
			.exchange()
			.expectStatus()
			.isOk();
		
		BDDMockito.verify(vendorRepository, never()).save(any());
	}
}
