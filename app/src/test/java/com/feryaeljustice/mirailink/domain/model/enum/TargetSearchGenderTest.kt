package com.feryaeljustice.mirailink.domain.model.enum

import org.junit.Assert.assertEquals
import org.junit.Test

class TargetSearchGenderTest {

    @Test
    fun `wireValue returns expected values`() {
        assertEquals("female", TargetSearchGender.FEMALE.wireValue)
        assertEquals("male", TargetSearchGender.MALE.wireValue)
        assertEquals("all", TargetSearchGender.ALL.wireValue)
    }

    @Test
    fun `fromWireValue parses correctly with fallback to ALL`() {
        assertEquals(TargetSearchGender.FEMALE, TargetSearchGender.fromWireValue("female"))
        assertEquals(TargetSearchGender.MALE, TargetSearchGender.fromWireValue("male"))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.fromWireValue("all"))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.fromWireValue("unknown"))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.fromWireValue(null))
    }

    @Test
    fun `defaultFor returns ALL by default for all users regardless of gender`() {
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor("male"))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor("female"))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor(Gender.Male))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor(Gender.Female))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor(null as String?))
        assertEquals(TargetSearchGender.ALL, TargetSearchGender.defaultFor(null as Gender?))
    }
}
