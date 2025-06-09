package com.aibracadabra.model.dto.client;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsDeserializer extends JsonDeserializer<TourInfoResponse.Items> {

	@Override
	public TourInfoResponse.Items deserialize(JsonParser p, DeserializationContext ctxt)
		throws IOException, JsonProcessingException {

		JsonNode node = p.getCodec().readTree(p);

		if (node.isTextual() && node.asText().isEmpty()) {
			return TourInfoResponse.Items.builder().item(List.of()).build();
		}

		JsonNode itemNode = node.get("item");
		List<TourInfoResponse.Item> itemList = new ArrayList<>();

		if (itemNode != null) {
			ObjectMapper mapper = (ObjectMapper) p.getCodec();
			if (itemNode.isArray()) {
				for (JsonNode n : itemNode) {
					TourInfoResponse.Item item = mapper.treeToValue(n, TourInfoResponse.Item.class);
					itemList.add(item);
				}
			} else if (itemNode.isObject()) {
				TourInfoResponse.Item item = mapper.treeToValue(itemNode, TourInfoResponse.Item.class);
				itemList.add(item);
			}
		}

		return TourInfoResponse.Items.builder().item(itemList).build();
	}
}
