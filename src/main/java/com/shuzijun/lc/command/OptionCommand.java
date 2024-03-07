package com.shuzijun.lc.command;

public class OptionCommand {
    private Option<?>[] options;

    public OptionCommand(Option<?>[] options) {
        this.options = options;
    }

    public Option<?>[] getOptions() {
        return options;
    }
}
