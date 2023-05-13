package com.SseApplication.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("Crawler")
public class WebCrawlerEntity {
		@Id
		private String crawlerId;
		private String url;
//		private boolean crawled;
		private String html;
		private boolean indexed;
		public WebCrawlerEntity(String href, String href2, boolean b) {
			this.url = href;
			this.html = href2;
			this.indexed = b;
		}
		
		

}
