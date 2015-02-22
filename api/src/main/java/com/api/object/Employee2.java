package com.api.object;

public class Employee2 {
  
  public Employee2(int id) {
    this.id = id;
  }

  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    
    if (o == this) return true;
    if (o == null) return false;
    if (this.getClass() != o.getClass()) return false;
    
   Employee2 e = (Employee2) o;
   
   return (this.getId() == e.getId());
  }
}
