# jyamlj

Java Yaml Json

Convert from json to yaml/json.

Useful if you dont understand yaml and dont but like json.

TODO:

* [x] json lexer
* [x] json parser
* [x] json renderer
* [x] yaml renderer
* [x] tests
* [x] publish/cli!

## Usage: 
```
jyamlj:
usage: jyamlj [-j] [-y]
-j: output json (default)
-y: output yaml
pipe the json you want to parse and render to stdin
```

## Building
In the root dir do `gradle assembleDist`.
You will find a `.tar` and a `.zip` in `build/distributions/`
you can unzip these files and you will find a bin folder with 2 files:
`jyamlj` and `jyamlj.bat`
use the jyamlj file in unixy systems and jyamlj.bat on Windows to run jyamlj.

## Example Usage:

### Yaml Output: 
```
❯ echo "{\"a\":[1,2,3]}" |  ./jyamlj -y
a: 	- 1
	- 2
	- 	- 3
	- 	
```

### Json Output:
```
❯ echo "{\"a\":[1,2,3]}" |  ./jyamlj -j
{
	"a": [
		1,
		2,
		3
	]
}
```

