package com.win.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Cli {

    private static final Logger log = Logger.getLogger(Cli.class.getName());
    private String[] args = null;
    private Options options = new Options();
    private String action;
    private String filter;

    public Cli(String[] args) {
        this.args = args;

        options.addOption("h", "help", false, "show help.");
        options.addOption("a", "action", true, "Here you can set parameter 'amend' or 'findout'.");
        options.addOption("f", "filter", true, "Here you can set parameter 'MINI_GAME_POPUP'.");
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void actions() {
        CommandLineParser parser = new BasicParser();

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("h")){
                help();
            }

            if (cmd.hasOption("a") || cmd.hasOption("f")) {
                log.log(Level.INFO, "Using argument -a=" + cmd.getOptionValue("a"));
                action = cmd.getOptionValue("a");
                log.log(Level.INFO, "Using argument -f=" + cmd.getOptionValue("f"));
                filter = cmd.getOptionValue("f");
            } else {
                log.log(Level.SEVERE, "Missing a option");
                help();
            }
        } catch (ParseException e) {
            log.log(Level.SEVERE, "Failed to parse comand line properties", e);
            help();
        }
    }

    private void help() {
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("Main", options);
        System.exit(0);
    }

}

