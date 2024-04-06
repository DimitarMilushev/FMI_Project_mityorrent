package main.java.application.types;

import main.java.commands.Command;
import main.java.commands.NetworkCommand;

import java.util.Date;

public record QueuedCommand(NetworkCommand instance, Date dateIssued) {}
