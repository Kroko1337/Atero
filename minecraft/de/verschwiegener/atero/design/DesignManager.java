package de.verschwiegener.atero.design;

import java.util.ArrayList;
import java.util.function.Predicate;

import de.verschwiegener.atero.design.designs.atero.AteroDesign;

public class DesignManager {

	public ArrayList<Design> designs = new ArrayList();

	public DesignManager() {
		designs.add(new AteroDesign());
	}

	public Design getDesignByName(final String name) {
		return designs.stream().filter(new Predicate<Design>() {
			@Override
			public boolean test(Design module) {
				return module.getName().equalsIgnoreCase(name);
			}
		}).findFirst().orElse(null);
	}

}
