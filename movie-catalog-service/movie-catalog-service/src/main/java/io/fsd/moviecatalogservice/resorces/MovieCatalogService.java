package io.fsd.moviecatalogservice.resorces;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.fsd.moviecatalogservice.model.CatalogItem;
import io.fsd.moviecatalogservice.model.Movie;
import io.fsd.moviecatalogservice.model.Rating;
import io.fsd.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogService {

	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {
		
		UserRating ratings = restTemplate.getForObject("http://rating-info/ratingsdata/user/"+userId, UserRating.class);
       
		return ratings.getRatings().stream()
                .map(rating ->{ 
                	Movie movie=restTemplate.getForObject("http://movie-info/movies/"+rating.getMovieId(), Movie.class);
                	return new CatalogItem(movie.getName(), "Desc", rating.getRating());
                	
                	})
                .collect(Collectors.toList());
    }
	
}
