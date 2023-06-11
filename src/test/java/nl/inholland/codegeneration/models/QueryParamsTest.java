package nl.inholland.codegeneration.models;

import org.hibernate.query.SemanticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import nl.inholland.codegeneration.exceptions.APIException;

// import javax.persistence.criteria.CriteriaBuilder;
// import javax.persistence.criteria.CriteriaQuery;
// import javax.persistence.criteria.Root;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryParamsTest {

  private QueryParams<Object> queryParams;

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private User mockUser;

  // @Mock
  // private Root<Object> root;

  // @Mock
  // private CriteriaQuery<?> query;

  // @Mock
  // private CriteriaBuilder builder;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    queryParams = new QueryParams<>(Object.class, 10, 1);
  }

  @Test
void testAddFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteria = new FilterCriteria("sampleKey", ":", "sampleValue");

    assertTrue(queryParams.addFilter(filterCriteria));
}

@Test
void testBuildFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    queryParams.setFilter("sampleKey:>:'sampleValue'");

    assertNotNull(queryParams.buildFilter());
}

  @Test
  void testGetClassReference() {
    assertEquals(Object.class, queryParams.getClassReference());
  }

  @Test
    void testGetFilterCriteria() throws Exception {
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

        queryParams.setFilter("sampleKey:>:'sampleValue'");

        assertEquals(1, queryParams.getFilterCriteria().size());
    }

  @Test
  void testGetLimit() {
    assertEquals(10, queryParams.getLimit());
  }

  @Test
  void testGetPage() {
    assertEquals(1, queryParams.getPage());
  }

  @Test
  void testSetClassReference() {
    queryParams.setClassReference(String.class);
    assertEquals(String.class, queryParams.getClassReference());
  }

  @Test
    void testSetFilter() throws Exception {
        when(authentication.getPrincipal()).thenReturn(mockUser);
        when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

        String filterQuery = "limit>:'10'";
        queryParams.setFilter(filterQuery);

        assertFalse(queryParams.getFilterCriteria().isEmpty());
    }

  @Test
  void testSetLimit() {
    queryParams.setLimit(5);
    assertEquals(5, queryParams.getLimit());
  }

  @Test
  void testSetPage() {
    queryParams.setPage(2);
    assertEquals(2, queryParams.getPage());
  }

  @Test
void testUnauthorizedAddFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.emptyList());

    FilterCriteria filterCriteria = new FilterCriteria("sampleKey", ":", "sampleValue");

    assertThrows(BadCredentialsException.class, () -> queryParams.addFilter(filterCriteria));
}

@Test
void testInvalidFilterOptionAddFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteria = new FilterCriteria("invalidKey", ":", "sampleValue");

    assertThrows(SemanticException.class, () -> queryParams.addFilter(filterCriteria));
}

@Test
void testInvalidFilterFieldTypeBuildFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteria = new FilterCriteria("sampleKey", ":", "unsupportedFieldTypeValue");
    queryParams.addFilter(filterCriteria);

    assertThrows(APIException.class, () -> queryParams.buildFilter());
}

@Test
void testInvalidFilterQuerySetFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    String invalidFilterQuery = "invalidKey:>:'sampleValue'";

    assertThrows(Exception.class, () -> queryParams.setFilter(invalidFilterQuery));
}

@Test
void testProcessDifferentDataTypesAddFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteriaString = new FilterCriteria("stringKey", ":", "sampleValue");
    FilterCriteria filterCriteriaInteger = new FilterCriteria("integerKey", ":", "10");

    assertTrue(queryParams.addFilter(filterCriteriaString));
    assertTrue(queryParams.addFilter(filterCriteriaInteger));
}

@Test
void testProcessNestedFilterablePropertiesAddFilter() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteria = new FilterCriteria("address.city", ":", "sampleCity");

    assertTrue(queryParams.addFilter(filterCriteria));
}

@Test
void testFieldHasBeenAdded() throws Exception {
    when(authentication.getPrincipal()).thenReturn(mockUser);
    when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

    FilterCriteria filterCriteria = new FilterCriteria("sampleKey", ":", "sampleValue");
    queryParams.addFilter(filterCriteria);

    assertTrue(queryParams.fieldHasBeenAdded("sampleKey"));
}
}

// package nl.inholland.codegeneration.models;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContext;
// import org.springframework.security.core.context.SecurityContextHolder;

// import java.util.Collections;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// class QueryParamsTest {

// private QueryParams<Object> queryParams;

// @Mock
// private Authentication authentication;

// @Mock
// private SecurityContext securityContext;

// @Mock
// private User mockUser;

// @BeforeEach
// void setUp() {
// MockitoAnnotations.openMocks(this);

// when(securityContext.getAuthentication()).thenReturn(authentication);
// SecurityContextHolder.setContext(securityContext);

// queryParams = new QueryParams<>(Object.class, 10, 1);
// }

// @Test
// void setFilterSuccessfully() throws Exception {
// when(authentication.getPrincipal()).thenReturn(mockUser);
// when(mockUser.getRoles()).thenReturn(Collections.singletonList(Role.EMPLOYEE));

// String filterQuery = "sampleFilter:>:'value'";
// queryParams.setFilter(filterQuery);

// assertFalse(queryParams.getFilterCriteria().isEmpty());
// }

// @Test
// void addFilterUnauthorizedException() {
// when(authentication.getPrincipal()).thenReturn(null);

// FilterCriteria filterCriteria = new FilterCriteria("sampleKey", ":",
// "sampleValue");

// assertThrows(BadCredentialsException.class, () ->
// queryParams.addFilter(filterCriteria));
// }

// @Test
// void testAddFilter() {

// }

// @Test
// void testBuildFilter() {

// }

// @Test
// void testGetClassReference() {

// }

// @Test
// void testGetFilterCriteria() {

// }

// @Test
// void testGetLimit() {

// }

// @Test
// void testGetPage() {

// }

// @Test
// void testSetClassReference() {

// }

// @Test
// void testSetFilter() {

// }

// @Test
// void testSetLimit() {

// }

// @Test
// void testSetPage() {

// }

// }

// // package nl.inholland.codegeneration.models;

// // import static org.junit.jupiter.api.Assertions.fail;

// // import org.junit.jupiter.api.Test;

// // public class QueryParamsTest {
// // @Test
// // void testAddFilter() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testBuildFilter() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testGetClassReference() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testGetFilterCriteria() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testGetLimit() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testGetPage() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testSetClassReference() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testSetFilter() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testSetLimit() {
// // fail("Not yet implemented");

// // }

// // @Test
// // void testSetPage() {
// // fail("Not yet implemented");

// // }
// // }
