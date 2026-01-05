package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtenteTest {

    @Test
    void testSetHashAtPassword() {
        Utente utente = new Utente();
        String passwordInChiaro = "Password123!";

        utente.setHashAtPassword(passwordInChiaro);

        assertNotNull(utente.getPasswordHash());
        assertNotEquals(passwordInChiaro, utente.getPasswordHash(), "La password deve essere hashata, non in chiaro");
    }

    @Test
    void testValidaPasswordCorretta() {
        Utente utente = new Utente();
        String password = "SecurePassword!";
        utente.setHashAtPassword(password);

        assertTrue(utente.validaPassword(password), "La validazione deve avere successo con la password corretta");
    }

    @Test
    void testValidaPasswordErrata() {
        Utente utente = new Utente();
        utente.setHashAtPassword("Miao123!");

        assertFalse(utente.validaPassword("Bau123!"), "La validazione deve fallire con password errata");
    }

    @Test
    void testGetterSetter() {
        Utente u = new Utente();
        u.setUsername("testUser");
        u.setEmail("test@email.com");
        u.setEta(25);

        assertEquals("testUser", u.getUsername());
        assertEquals("test@email.com", u.getEmail());
        assertEquals(25, u.getEta());
    }
}