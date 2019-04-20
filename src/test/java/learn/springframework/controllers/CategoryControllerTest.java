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
import learn.springframework.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CategoryControllerTest {

	WebTestClient webTestClient;
	CategoryRepository categoryRepository;
	CategoryController categoryController;

	@Before
	public void setUp() throws Exception {
		categoryRepository = Mockito.mock(CategoryRepository.class);
		categoryController = new CategoryController(categoryRepository);
		webTestClient = WebTestClient.bindToController(categoryController).build();
	}

	@Test
	public void list() {
		Category category1 = new Category();
		category1.setDescription("Cat1");
		
		Category category2 = new Category();
		category2.setDescription("Cat2");
		
		BDDMockito.given(categoryRepository.findAll())
			.willReturn(Flux.just(category1, category2));
		
		webTestClient.get()
			.uri("/api/v1/categories/")
			.exchange()
			.expectBodyList(Category.class)
			.hasSize(2);
	}

	@Test
	public void getById() {
		Category category1 = new Category();
		category1.setDescription("Cat1");
		
		BDDMockito.given(categoryRepository.findById("someId"))
			.willReturn(Mono.just(category1));
		
		webTestClient.get()
		.uri("/api/v1/categories/someId")
		.exchange()
		.expectBody(Category.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void create(){
		Category category1 = new Category();
		category1.setDescription("Cat1");
		
		BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
			.willReturn(Flux.just(category1));
		
		Mono<Category> catToSaveMono = Mono.just(category1);
		
		webTestClient.post()
			.uri("/api/v1/categories")
			.body(catToSaveMono, Category.class)
			.exchange()
			.expectStatus()
			.isCreated();
	}
	
	@Test
	public void update() {
		Category category1 = new Category();
		category1.setDescription("Cat1");
		
		BDDMockito.given(categoryRepository.save(any(Category.class)))
			.willReturn(Mono.just(category1));
		
		Mono<Category> catToUpdateMono  = Mono.just(category1);
		
		webTestClient.put()
			.uri("/api/v1/categories/someid")
			.body(catToUpdateMono, Category.class)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	@Test
	public void testPatchWithChanges() {
		Category category1 = new Category();
		category1.setId("someid");
		category1.setDescription("Cat1");
		
		BDDMockito.given(categoryRepository.findById(anyString()))
			.willReturn(Mono.just(category1));
		
		BDDMockito.given(categoryRepository.save(any(Category.class)))
			.willReturn(Mono.just(category1));
		
		Category category2 = new Category();
		category2.setId("someid");
		category2.setDescription("Cat2");
		Mono<Category> catToUpdateMono  = Mono.just(category2);
		
		webTestClient.patch()
			.uri("/api/v1/categories/someid")
			.body(catToUpdateMono, Category.class)
			.exchange()
			.expectStatus()
			.isOk();
		
		BDDMockito.verify(categoryRepository).save(any());
	}
	
	@Test
	public void testPatchNoChanges() {
		Category category1 = new Category();
		category1.setId("someid");
		category1.setDescription("Cat1");
		
		BDDMockito.given(categoryRepository.findById(anyString()))
			.willReturn(Mono.just(category1));
		
		BDDMockito.given(categoryRepository.save(any(Category.class)))
			.willReturn(Mono.just(category1));
		
		Mono<Category> catToUpdateMono = Mono.just(category1);
		
		webTestClient.patch()
			.uri("/api/v1/categories/someid")
			.body(catToUpdateMono, Category.class)
			.exchange()
			.expectStatus()
			.isOk();
		
		BDDMockito.verify(categoryRepository, never()).save(any());
	}
}
