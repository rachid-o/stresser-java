## On OSX when jdk9 is installed, but not default set the JAVA_HOME.
## export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-9.0.1.jdk/Contents/Home && java -version


#javac -d out --module-source-path stresser.generator.cpu/src -m stresser.generator.cpu

#echo javac -d out --module-source-path $(find . -type d -name src) -m stresser.generator.cpu

#javac -d out stresser.generator.cpu/src

javac -d out --module-source-path stresser.generator.implementations/src:stresser.cli/src \
 $(find stresser.cli/src -name *.java)


exit

rm -rf mods && mkdir mods;

jcompile() {
 echo compiling module: $1
 javac -d out/$1  $(find $1/src -name *.java) #-m $1
 echo packaging: $1
 jar -cfe mods/$1.jar -C out/$1
}

jcompile stresser.generator.cpu
#jcompile stresser.cli


ls out
