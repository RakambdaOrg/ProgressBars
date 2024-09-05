open module fr.rakambda.progressbar {
	requires static lombok;
	requires static org.jetbrains.annotations;
	
	requires org.jline.terminal;
	requires org.slf4j;
	
	exports fr.rakambda.progressbar.api.bar;
	exports fr.rakambda.progressbar.api.render;
	exports fr.rakambda.progressbar.api.update;
	exports fr.rakambda.progressbar.impl.bar;
	exports fr.rakambda.progressbar.impl.holder;
	exports fr.rakambda.progressbar.impl.render;
	exports fr.rakambda.progressbar.impl.update;
}