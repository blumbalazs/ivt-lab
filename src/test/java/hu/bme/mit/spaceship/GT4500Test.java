package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy.SinglePass;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;

  private TorpedoStore mockPrimaryTorperodeStore = mock(TorpedoStore.class,withSettings().useConstructor(10));
  private TorpedoStore mockSecondaryTorperodeStore = mock(TorpedoStore.class,withSettings().useConstructor(10));
  @BeforeEach
  public void init(){

    this.ship = new GT4500(mockPrimaryTorperodeStore,mockSecondaryTorperodeStore);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(0)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange

    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(true);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedoSingleFailure(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(0)).fire(1);
    assertEquals(false, result);
  }
  @Test
  public void fireSecondaryTorpedoSuccess(){

    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(true);
    //torpedo váltáshoz
    ship.fireTorpedo(FiringMode.SINGLE);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(1)).fire(1);
    assertEquals(true, result);
  }
  @Test
  public void firePrimaryTorpedoSuccess(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(0)).fire(1);
    assertEquals(true, result);
  }
  @Test
  public void fireAllTorpedoStoreWithOneFilledSuccess(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(1)).fire(1);
    assertEquals(true, result);
  }
  @Test
  public void fireAllTorpedoStoreFailure(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimaryTorperodeStore,times(1)).fire(1);
    verify(mockSecondaryTorperodeStore,times(1)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedoAfterTorpedoStoresAreEmpty(){
      // Arrange
      when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
      when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
      when(mockPrimaryTorperodeStore.isEmpty()).thenReturn(true);
      when(mockSecondaryTorperodeStore.isEmpty()).thenReturn(true);

      //Act
      boolean result = ship.fireTorpedo(FiringMode.ALL);
      
      //Assert

      assertEquals(false, result);
  }

  @Test
  public void fireLaser(){
    // Arrange
    
    //Act
    boolean result = ship.fireLaser(FiringMode.SINGLE);
    //Assert
    assertEquals(false, result);
  }
  @Test
  public void fireSecondaryTorpedoStoreWhenEmptyPrimaryHasTorpedo(){
    // Arrange
    //Mock valami oknál fogva be cachelődött és a TorpedoStore.isEmpty függvénye mindig trueval tért vissza
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockPrimaryTorperodeStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorperodeStore.isEmpty()).thenReturn(true);

    //primary lövés hogy következő secondary legyen
    ship.fireTorpedo(FiringMode.SINGLE);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    //Assert
    assertEquals(true, result);
  }
  @Test
  public void firePrimaryTorpedoStoreWhenEmptySecondaryHasTorpedo(){
    // Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(true);
    when(mockPrimaryTorperodeStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorperodeStore.isEmpty()).thenReturn(false);

    //Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    //Assert
    assertEquals(true, result);
  }
  @Test
  public void fireAllWhenEmpty(){
    //Arrange
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockPrimaryTorperodeStore.isEmpty()).thenReturn(false);
    when(mockSecondaryTorperodeStore.isEmpty()).thenReturn(false);
    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    //Assert
    assertEquals(false, result);
  }
  @Test
  public void fireAllWithPrimaryTorpedoStoreEmpty(){
    //Arrange
    when(mockPrimaryTorperodeStore.isEmpty()).thenReturn(true);
    when(mockSecondaryTorperodeStore.isEmpty()).thenReturn(false);
    when(mockPrimaryTorperodeStore.fire(1)).thenReturn(false);
    when(mockSecondaryTorperodeStore.fire(1)).thenReturn(true);
    //Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);
    //Assert
    assertEquals(true, result);
  }
}
