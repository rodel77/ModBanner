package mx.com.rodel.modbanner;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Helper {
	public static Text format(String string){
		return TextSerializers.FORMATTING_CODE.deserialize(string);
	}
}
