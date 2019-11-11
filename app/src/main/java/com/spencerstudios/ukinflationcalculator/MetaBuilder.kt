package com.spencerstudios.ukinflationcalculator

import java.util.*

class MetaBuilder {

    fun build(yearValOne: Int, yearValTwo: Int, userDefinedCashValue: Double): Array<String> {
        val meta = buildYearMeta()
        val valueOne = meta[meta.indexOfFirst { it.year == yearValOne }].value
        val valueTwo = meta[meta.indexOfFirst { it.year == yearValTwo }].value
        val adjustedCashValue = ((valueTwo / valueOne) * userDefinedCashValue)
        return arrayOf(
            String.format(Locale.getDefault(), "£%.2f", adjustedCashValue),
            String.format(Locale.getDefault(), "£%,.2f in %d is equivalent to £%,.2f in %d", userDefinedCashValue, yearValOne, adjustedCashValue, yearValTwo))
    }

    fun buildYearMeta() : ArrayList<Meta> {
        val meta = ArrayList<Meta>()

        meta.add(Meta(2000, 671.8))
        meta.add(Meta(2001, 683.7))
        meta.add(Meta(2002, 695.1))
        meta.add(Meta(2003, 715.2))
        meta.add(Meta(2004, 736.5))
        meta.add(Meta(2005, 757.3))
        meta.add(Meta(2006, 781.5))
        meta.add(Meta(2007, 815.0))
        meta.add(Meta(2008, 847.5))
        meta.add(Meta(2009, 843.0))
        meta.add(Meta(2010, 881.9))
        meta.add(Meta(2011, 927.8))
        meta.add(Meta(2012, 957.6))
        meta.add(Meta(2013, 986.7))
        meta.add(Meta(2014, 1010.0))
        meta.add(Meta(2015, 1020.0))
        meta.add(Meta(2016, 1037.7))
        meta.add(Meta(2017, 1074.9))
        meta.add(Meta(2018, 1101.59335))
        meta.add(Meta(2019, 1121.4220303))

        return meta
    }
}