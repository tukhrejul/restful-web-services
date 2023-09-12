package com.in28minutes.rest.webservices.restfulwebservices.filtering;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class FilteringController
{
    @GetMapping("/filtering")
    public SomeBean getFiltering()
    {
        return new SomeBean("value1", "value2", "value3");
    }

    @GetMapping("/filtering-list")
    public List<SomeBean> getFilteringList()
    {
        return Arrays.asList(new SomeBean("value1", "value2", "value3"),
                new SomeBean("value4", "value5", "value6"),
                new SomeBean("value7", "value8", "value9"),
                new SomeBean("value10", "value12", "value13"));
    }
}
