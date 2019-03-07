import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List

b = [(1,"A"), (3,"C")]

main = do
    let a = permutations "ABCDEFGH"
    let c = []
    returnForced a b c
    print c
  


returnForced :: [String] -> [(Integer,Char)] -> [String] -> [String]
returnForced [] b = 0
returnForced (x:xs) (y:ys) c 
| length(a) == 0 = c
|(checkForced head(a) b) = returnForced a tail(b) c+y
| otherwise = returnForced tail(a) b []
						
								

checkForced :: [String] -> [(x,y)] -> [Bool]
checkForced string1 forcedList =
if length(forcedList) == 0
	then True
else if(index(string1, fst(head(forcedList))) == snd(head(forcedList))) 
	then checkForced string1 tail(forcedList)
else False
								 

 