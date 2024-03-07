package com.shuzijun.lc.command;

public interface Option<T> {
   void Parse(T value);
    OptionType<T> Type();
}
