package agilor.distributed.storage.inter.thrift; /**
 * Autogenerated by Thrift Compiler (0.9.2)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */

import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum EXCEPTIONTYPE implements TEnum {
  INSERTFAIL(0),
  REGISTERFAIL(1);

  private final int value;

  private EXCEPTIONTYPE(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static EXCEPTIONTYPE findByValue(int value) { 
    switch (value) {
      case 0:
        return INSERTFAIL;
      case 1:
        return REGISTERFAIL;
      default:
        return null;
    }
  }
}
