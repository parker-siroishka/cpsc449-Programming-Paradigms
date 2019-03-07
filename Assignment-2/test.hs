import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List

b = [(1,"A"), (3,"C")]

main = do
    let a = permutations "ABCDEFGH"
    let c = getFAccepted b a []
    print c


forced = [(1, "A"), (2, "B")]



-- machine = “ABCDEFGH”


findIndex1 :: Char -> [Char] -> Integer -> Integer
findIndex1 character [] index = -1
findIndex1 character (x:xs) index 
    | character == x = index
    | otherwise = findIndex1 character xs index + 1


stringIsAccepted :: [(Integer,char)] -> [char] -> bool
stringIsAccepted [] machine = True
stringIsAccepted (x:xs) machine = (findIndex1 snd(x) machine 0 == fst x) && stringIsAccepted xs machine




getFAccepted :: [(Integer,char)] -> [char] -> [[char]] -> [[char]]

getFAccepted [] (y:ys) accepted = (y:ys)
getFAccepted (x:xs) [] accepted = accepted
getFAccepted (x:xs) (y:ys) accepted
    | stringIsAccepted (x:xs) y = getFAccepted (x:xs) ys accepted + y
    | otherwise = getFAccepted (x:xs) ys accepted