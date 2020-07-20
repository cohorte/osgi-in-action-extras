

Why does Eclipse find javax.swing but not Felix?
================================================

http://wiki.osgi.org/wiki/Why_does_Eclipse_find_javax.swing_but_not_Felix%3F




java.lang.NoClassDefFoundError on OSGi
======================================

http://stackoverflow.com/questions/5181211/java-lang-noclassdeffounderror-on-osgi

Use this VM argument:

-Djava.specification.version=1.6

This will force Felix to make available the default set of system packages for Java 6, which includes javax.swing and its sub-packages

