package com.infobip.jackson;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.CaseFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

@AllArgsConstructor
class PresentPropertyCaseFormatDeserializerTest extends TestBase {

    @Test
    void shouldDeserializeRoadBikeAsBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'road_bike':'road bike'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("road bike"));
    }

    @Test
    void shouldDeserializeRoadBikeAsBikeFromSerializedRoadBike() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(new RoadBike("road bike"));

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("road bike"));
    }

    @Test
    void shouldDeserializeMountainBikeAsBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'mountain_bike':'mountain bike'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new MountainBike("mountain bike"));
    }

    @Test
    void shouldDeserializeMountainBikeAsBikeFromSerializedMountainBike() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(new MountainBike("mountain bike"));

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new MountainBike("mountain bike"));
    }

    @Test
    void shouldDeserializeListOfBikes() throws JsonProcessingException {
        // given
        String json = objectMapper.writeValueAsString(
                Arrays.asList(new RoadBike("road bike"), new MountainBike("mountain bike")));

        // when
        List<Bike> actual = objectMapper.readValue(json, new TypeReference<List<Bike>>() {
        });

        // then
        then(actual).isEqualTo(Arrays.asList(new RoadBike("road bike"), new MountainBike("mountain bike")));
    }

    @Test
    void shouldDeserializeBikeWithMultipleMatchingProperties() throws JsonProcessingException {
        // given
        String json = "{'mountain_bike':'', 'road_bike': 'road bike'}";

        // when
        Bike actual = objectMapper.readValue(json, Bike.class);

        // then
        then(actual).isEqualTo(new RoadBike("road bike"));
    }

    @Test
    void shouldDeserializeRoadBikeAsRoadBikeFromJson() throws JsonProcessingException {
        // given
        String json = "{'road_bike':'road bike'}";

        // when
        RoadBike actual = objectMapper.readValue(json, RoadBike.class);

        // then
        then(actual).isEqualTo(new RoadBike("road bike"));
    }

    @JsonTypeResolveWith(LowerUnderscorePresentPropertyJsonTypeResolver.class)
    interface Bike extends PresentPropertyJsonHierarchy<BikeType> {

    }

    static class LowerUnderscorePresentPropertyJsonTypeResolver<E extends Enum<E> & TypeProvider<E>>
            extends PresentPropertyJsonTypeResolver<E> {

        public LowerUnderscorePresentPropertyJsonTypeResolver(Class<E> type) {
            super(type, CaseFormat.LOWER_UNDERSCORE);
        }
    }

    record RoadBike(@JsonProperty("road_bike") String roadBike) implements Bike {

    }

    record MountainBike(@JsonProperty("mountain_bike") String mountainBike) implements Bike {

    }

    @Getter
    @AllArgsConstructor
    enum BikeType implements TypeProvider<Bike> {
        ROAD_BIKE(RoadBike.class),
        MOUNTAIN_BIKE(MountainBike.class);

        private final Class<? extends Bike> type;
    }
}
