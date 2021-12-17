package org.generation.blogPessoal.configuration;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration
public class SwaggerConfiguration {

	@Bean
	public OpenAPI springBlogPessoalOpenAPI() {
		return new OpenAPI()
				.info(new Info()
					.title("Projeto Blog Pessoal")
					.description("Projeto de Blog Pessoal realizado durante o Bootcamp Generation")
					.version("v0.0.1")
				.license(new License()
						.name("Generation Brasil")
						.url("https://brazil.generation.org/"))
				.contact(new Contact()
						.name("Fabrício Rocha")
						.url("https://github.com/fabricior0cha")
						.email("fabriciorochapalmeira@gmail.com")))
				.externalDocs(new ExternalDocumentation()
						.description("GitHub Blog Pessoal")
						.url("https://github.com/fabricior0cha/Blog-pessoal"));
						
		
					
	}

	private ApiResponse createApiResponse(String message) {
		
		return new ApiResponse().description(message);
	}
	
	@Bean
	public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
		
		return openApi -> {
			openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
						
				ApiResponses apiResponses = operation.getResponses();
				
				apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
				apiResponses.addApiResponse("201", createApiResponse("Objeto criado!"));
				apiResponses.addApiResponse("204", createApiResponse("Objeto excluído!"));
				apiResponses.addApiResponse("400", createApiResponse("Erro na requisição!"));
				apiResponses.addApiResponse("401", createApiResponse("Não autorizado!"));
				apiResponses.addApiResponse("404", createApiResponse("Não encontrado!"));
				apiResponses.addApiResponse("500", createApiResponse("Erro na aplicação!"));
			}));
		};
	}
	
}
