/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mobile;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests
{

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MobileRepository mobileRepository;

	@Before
	public void deleteAllBeforeTests() throws Exception
	{
		mobileRepository.deleteAll();
	}

	//Check initial directory
	@Test
	public void shouldReturnRepositoryIndex() throws Exception
	{

		mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk()).andExpect(
				jsonPath("$._links.mobiles").exists());
	}

	//Assess to see if data can be added
	@Test
	public void shouldCreateEntity() throws Exception
	{
		mockMvc.perform(post("/mobiles").contentType(MediaType.APPLICATION_JSON)
				.content(
				"{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(
				status().isCreated()).andExpect(
				header().string("Location", containsString("mobiles/")));
	}

	//Assess if data can be added & then retrieved
	@Test
	public void shouldRetrieveEntity() throws Exception
	{
		MvcResult mvcResult  = mockMvc.perform(post("/mobiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(
				status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.msisdn").value("35699464108")).andExpect(
				jsonPath("$.customerIdOwner").value(2)).andExpect(
				jsonPath("$.serviceType").value("MOBILE_PREPAID")).andExpect(
				jsonPath("$.serviceStartDate").value(3203532));
	}

	//Assess to see if when trying to retrieve 1st record from a set with no data, 404 is returned
	@Test
	public void shouldReturnNotFound() throws Exception
	{
		mockMvc.perform(get("http://localhost/mobiles/1")).andExpect(status().isNotFound());
	}

	//Assess for data conflict by adding two records with same msisdn
	@Test
	public void shouldReturnConflict() throws Exception
	{
		mockMvc.perform(post("/mobiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(
				status().isCreated()).andReturn();

		mockMvc.perform(post("/mobiles").contentType(MediaType.APPLICATION_JSON)
				.content(
						"{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 7, \"customerIdUser\" : 8, \"serviceType\" : \"MOBILE_POSTPAID\", \"serviceStartDate\" : \"158435467\" }")
		).andExpect(
				status().isConflict());
	}

	//Assess to see if PUT operation works as intended
	@Test
	public void shouldUpdateEntity() throws Exception
	{
		MvcResult mvcResult  = mockMvc.perform(post("/mobiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");

		mockMvc.perform(put(location)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464104\", \"customerIdOwner\" : 8, \"customerIdUser\" : 7, \"serviceType\" : \"MOBILE_POSTPAID\", \"serviceStartDate\" : \"3203532\" }"))
				.andExpect(
				status().isNoContent());

		mockMvc.perform(get(location)).andExpect(status().isOk()).andExpect(
				jsonPath("$.msisdn").value("35699464108")).andExpect(
				jsonPath("$.customerIdOwner").value(2)).andExpect(
				jsonPath("$.serviceType").value("MOBILE_POSTPAID")).andExpect(
				jsonPath("$.serviceStartDate").value(3203532));
	}

	//Assess to see if PUT 404 error can be triggered when trying to edit non-existing data
	@Test
	public void shouldUpdateButSend404Error() throws Exception
	{
		mockMvc.perform(put("http://localhost/mobiles/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464104\", \"customerIdOwner\" : 8, \"customerIdUser\" : 7, \"serviceType\" : \"MOBILE_POSTPAID\", \"serviceStartDate\" : \"3203532\" }"))
				.andExpect(
						status().isNotFound());
	}

	//Assess to see if DELETE operation works as intended
	@Test
	public void shouldDeleteEntity() throws Exception
	{

		MvcResult mvcResult  = mockMvc.perform(post("/mobiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().is2xxSuccessful());

		mockMvc.perform(get(location)).andExpect(status().isNotFound());
	}

	//Assess to see if 404 error is triggered when deleting
	@Test
	public void shouldNotFindNonExistingFile() throws Exception
	{

		MvcResult mvcResult  = mockMvc.perform(post("/mobiles")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{ \"msisdn\" : \"35699464108\", \"customerIdOwner\" : 2, \"customerIdUser\" : 5, \"serviceType\" : \"MOBILE_PREPAID\", \"serviceStartDate\" : \"3203532\" }")
		).andExpect(status().isCreated()).andReturn();

		String location = mvcResult.getResponse().getHeader("Location");
		mockMvc.perform(delete(location)).andExpect(status().is2xxSuccessful());

		mockMvc.perform(delete(location)).andExpect(status().isNotFound());
	}
}