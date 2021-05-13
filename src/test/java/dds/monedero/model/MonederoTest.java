package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }
  Movimiento unMovimiento = new Movimiento(LocalDate.now(),500,true);
  Movimiento otroMovimiento = new Movimiento(LocalDate.now(),500, false);
  @Test
  public void Poner() {
    cuenta.poner(1500);
    Assertions.assertEquals(1500,cuenta.getSaldo());
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void TresDepositos() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    Assertions.assertEquals(3,cuenta.getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count());
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void ObtenerMontoDelDia(){
    cuenta.poner(1500);
    cuenta.sacar(500);
    cuenta.sacar(100);
    cuenta.sacar(245);
    Assertions.assertEquals(845,cuenta.getMontoExtraidoA(LocalDate.now()));
  }

  @Test
  public void EsDeposito() {
    Assertions.assertTrue(unMovimiento.isDeposito());
  }

  @Test
  public void FueDepositadoHoy() {
    Assertions.assertTrue( unMovimiento.esDeLaFecha(LocalDate.now()) && unMovimiento.isDeposito());
  }
  @Test
  public void FueExtraidoHoy() {
    Assertions.assertTrue( otroMovimiento.fueExtraido(LocalDate.now()));
  }
}
