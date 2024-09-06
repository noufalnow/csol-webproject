@Test
public void testValidationErrors() throws Exception {
    CoreUserDTO dto = new CoreUserDTO();
    dto.setUserPassword("short"); // Invalid password length
    dto.setUserPasswordConf("mismatch"); // Passwords do not match

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors.userPassword").value("Password must be at least 6 characters"))
            .andExpect(jsonPath("$.validationErrors.userPasswordConf").value("Passwords do not match"));
}
