package kae.demo.transfer.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/** */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

  private long id;

  private String name;
}
