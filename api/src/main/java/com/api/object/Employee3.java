package com.api.object;

public class Employee3 {
  
  public Employee3(int id) {
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
    
   Employee3 e = (Employee3) o;
   
   return (this.getId() == e.getId());
  }
  
  @Override
  public int hashCode() {
    final int PRIME = 31;
    int result = 1;
    
    result = PRIME * result + getId();
    
    return result;
  }
}
