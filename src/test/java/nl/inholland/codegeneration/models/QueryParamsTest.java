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
