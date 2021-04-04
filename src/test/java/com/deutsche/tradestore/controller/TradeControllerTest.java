package com.deutsche.tradestore.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.deutsche.tradestore.data.TradeStorePayload;
import com.deutsche.tradestore.data.TradeStoreResponse;
import com.deutsche.tradestore.entity.Trade;
import com.deutsche.tradestore.entity.TradeStoreId;
import com.deutsche.tradestore.exception.LowerVersionReceivedException;
import com.deutsche.tradestore.exception.TradestoreException;
import com.deutsche.tradestore.service.TradeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(controllers = TradeController.class)
public class TradeControllerTest {

	/*
	 * @Autowired private MockMvc mockMvc;
	 * 
	 * @Mock private TradeService tradeService;
	 * 
	 * @Autowired WebApplicationContext webApplicationContext;
	 */
	@MockBean
	private TradeService tradeService;
	@InjectMocks
	private TradeController tradeController;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

	}

	@Test
	public void testgetAllTradeStoreDetails() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		String curentTime = "03/04/2021";
		Date createdDate = new Date(sdf.parse(curentTime).getTime());
		Trade trade = new Trade();
		TradeStoreId tradeStoreId = new TradeStoreId("T1", 1);
		trade.setTradeStoreId(tradeStoreId);
		trade.setCounterPartyId("CP1");
		trade.setBookId("B1");
		trade.setMaturityDate(maturityDate);
		trade.setCreatedDate(createdDate);

		Trade trade1 = new Trade();
		trade.setTradeStoreId(tradeStoreId);
		trade1.setCounterPartyId("CP1");
		trade1.setBookId("B1");
		trade1.setMaturityDate(maturityDate);
		trade1.setCreatedDate(createdDate);

		List<Trade> tradeList = new ArrayList<>();
		tradeList.add(trade);
		tradeList.add(trade1);
		Mockito.when(tradeService.getAllTradeDetails()).thenReturn(tradeList);

		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/tradedetails").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
		ObjectMapper objectMapper = new ObjectMapper();
		String expectedResult = mvcResult.getResponse().getContentAsString();
		Trade[] expectedTradeList = objectMapper.readValue(expectedResult, Trade[].class);
		assertEquals(2, expectedTradeList.length);

	}

	@Test
	public void testgetAllTradeStoreDetailsWIthNullResults() throws Exception {
		List<Trade> tradeList = new ArrayList<>();
		Mockito.when(tradeService.getAllTradeDetails()).thenReturn(tradeList);
		MvcResult mvcResult = mockMvc
				.perform(MockMvcRequestBuilders.get("/tradedetails").accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		assertEquals(200, mvcResult.getResponse().getStatus());
		ObjectMapper objectMapper = new ObjectMapper();
		String expectedResult = mvcResult.getResponse().getContentAsString();
		Trade[] expectedTradeList = objectMapper.readValue(expectedResult, Trade[].class);
		assertEquals(0, expectedTradeList.length);
	}

	@Test
	public void testsaveTradeDetails() throws Exception, TradestoreException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, null);
		TradeStoreResponse tradeStoreResponse = new TradeStoreResponse();
		tradeStoreResponse.setTradeId("T1");
		tradeStoreResponse.setVersion(1);
		tradeStoreResponse.setMessage("CREATED");

		Mockito.when(tradeService.storeTradeDetails(Mockito.any(TradeStorePayload.class)))
				.thenReturn(tradeStoreResponse);
		ObjectMapper objectMapper = new ObjectMapper();

		String inputJson = objectMapper.writeValueAsString(tradeDetail);

		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value("T1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("CREATED"));

	}

	@Test
	public void testsaveTradeDetailsThrowsLowerVersionReceivedException() throws Exception, TradestoreException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, null);

		Mockito.when(tradeService.storeTradeDetails(Mockito.any(TradeStorePayload.class)))
				.thenThrow(LowerVersionReceivedException.class);
		ObjectMapper objectMapper = new ObjectMapper();

		String inputJson = objectMapper.writeValueAsString(tradeDetail);

		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value("T1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message")
						.value("com.deutsche.tradestore.exception.LowerVersionReceivedException"));

	}

	@Test
	public void testsaveTradeDetailsThrowsTradestoreException() throws Exception, TradestoreException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", maturityDate, null);

		Mockito.when(tradeService.storeTradeDetails(Mockito.any(TradeStorePayload.class)))
				.thenThrow(TradestoreException.class);
		ObjectMapper objectMapper = new ObjectMapper();

		String inputJson = objectMapper.writeValueAsString(tradeDetail);

		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value("T1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1)).andExpect(MockMvcResultMatchers
						.jsonPath("$.message").value("com.deutsche.tradestore.exception.TradestoreException"));

	}

	@Test
	public void testsaveTradeDetailsWithValidationErrorsForTradeId() throws Exception, TradestoreException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload(null, 1, "B1", "CP1", maturityDate, null);

		ObjectMapper objectMapper = new ObjectMapper();

		String inputJson = objectMapper.writeValueAsString(tradeDetail);

		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
				.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value(IsNull.nullValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
						"[tradeId cannot be null]"));

	}
	@Test
	public void testsaveTradeDetailsWithValidationErrorsForMaturityDate() throws Exception, TradestoreException {
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", 1, "B1", "CP1", null, null);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String inputJson = objectMapper.writeValueAsString(tradeDetail);
		
		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
		.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value("T1"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
				"[maturitydate cannot be null]"));
		
	}
	@Test
	public void testsaveTradeDetailsWithValidationErrorsForVersion() throws Exception, TradestoreException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String mdString = "02/03/2021";
		Date maturityDate = new Date(sdf.parse(mdString).getTime());
		TradeStorePayload tradeDetail = new TradeStorePayload("T1", -1, "B1", "CP1", maturityDate, null);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		String inputJson = objectMapper.writeValueAsString(tradeDetail);
		
		mockMvc.perform(post("/savetradedetails").accept(MediaType.APPLICATION_JSON).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(200))
		.andExpect(MockMvcResultMatchers.jsonPath("$.tradeId").value("T1"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.version").value(-1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(
				"[version must be equal to or greater than 1]"));
		
	}
}
