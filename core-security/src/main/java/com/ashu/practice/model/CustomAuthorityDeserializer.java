package com.ashu.practice.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.LinkedList;
import java.util.List;

public class CustomAuthorityDeserializer extends ValueDeserializer<Object> {

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) {
		JsonNode jsonNode =ctxt.readTree(jp);
		List<GrantedAuthority> grantedAuthorities = new LinkedList<>();

        for (JsonNode next : jsonNode) {
            JsonNode authority = next.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.stringValue()));
        }
		return grantedAuthorities;
	}
}
