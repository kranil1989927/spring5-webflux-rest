package learn.springframework.bootstrap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;

import learn.springframework.domain.Category;
import learn.springframework.domain.Vendor;
import learn.springframework.repositories.CategoryRepository;
import learn.springframework.repositories.VendorRepository;

public class Bootstrap implements CommandLineRunner {

	private final CategoryRepository categoryRepository;
	private final VendorRepository vendorRepository;

	public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
		this.categoryRepository = categoryRepository;
		this.vendorRepository = vendorRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		if (categoryRepository.count().block() == 0) {
			// load data
			System.out.println("#### LOADING DATA ON BOOTSTRAP #####");
			loadCategories();
			System.out.println("Loaded Categories: " + categoryRepository.count().block());

			loadVendors();
			System.out.println("Loaded Vendors: " + vendorRepository.count().block());
		}

	}

	private void loadCategories() {
		String categoryType[] = { "Fruits", "Nuts", "Breads", "Meats", "Eggs" };
		List<String> categories = Arrays.stream(categoryType).collect(Collectors.toList());

		categories.forEach(type -> {
			Category category = new Category();
			category.setDescription(type);
			categoryRepository.save(category).block();
		});
	}

	private void loadVendors() {
		List<Vendor> vendors = new ArrayList<Vendor>();
		vendors.add(new Vendor("Joe", "Buck"));
		vendors.add(new Vendor("Micheal", "Weston"));
		vendors.add(new Vendor("Jessie", "Waters"));
		vendors.add(new Vendor("Bill", "Nershi"));
		vendors.add(new Vendor("Jimmy", "Buffett"));

		vendors.forEach(vendor -> {
			vendorRepository.save(vendor).block();
		});
	}

}
