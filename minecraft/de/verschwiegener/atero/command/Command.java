package de.verschwiegener.atero.command;

public abstract class Command {

    String syntax, description;
    String[] complete;

    public Command(String syntax, String description, String[] complete) {
	this.syntax = syntax;
	this.description = description;
	this.complete = complete;
    }

    public abstract void onCommand(String[] args);

    public String getSyntax() {
	return syntax;
    }

    public String getDescription() {
	return description;
    }

    public String[] getComplete() {
	return complete;
    }

}
