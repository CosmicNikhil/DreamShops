package com.nikhil.dreamshops.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.hibernate.annotations.NaturalId;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;

	//email VARCHAR(255) UNIQUE,   -- The UNIQUE constraint ensures no duplicate emails
	@NaturalId
	private String email;
	private String password;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Cart cart;

	//orphanRemoval = true: This ensures that if an Order is removed from the orders list of a User, 
	//the corresponding Order entity will be automatically deleted from the database.

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Order> orders;

	@ManyToMany(fetch=FetchType.EAGER,cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH} )
	@JoinTable(
			name="user_roles"
			,joinColumns = @JoinColumn(name="user_id",referencedColumnName = "id")
			,inverseJoinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"))
	private Collection<Role> roles = new HashSet<>();


}




/*
 * Here are short and crisp points for `@NaturalId`:
 * 
 * 1. **Business Key**: Represents a unique, meaningful identifier from the
 * business domain (e.g., email, username). 
 * 2. **Immutable**: Generally considered unchangeable once set, though it can be updated if needed. 
 * 3.**Unique Constraint**: Ensures uniqueness, but does not replace the primary key (`@Id`). 
 * 4. **Performance**: Optimizes lookups based on natural
 * identifiers for faster queries. 
 * 5. **Not Auto-Generated**: Values are
 * typically manually set and meaningful within the domain.
 */

//if a Cart is no longer associated with a User 
//(i.e., if the Cart reference in User is set to null or 
//if the Cart is removed from the database via an update operation), 
//the corresponding Cart entity is automatically deleted (removed) from the database.

/*
 *User user = userRepository.findById(1L); // Fetch a user
 *user.setCart(null); // Remove the cart associated with the user
 *userRepository.save(user); // Saving the user will automatically delete the orphaned cart
 */
