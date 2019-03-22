import System.IO
import System.Environment
import System.Exit
import Data.Char
import Data.List
import Data.Tuple


forced = [(1,'A'),(2,'B'),(3,'C'),(4,'D'),(5,'E')]
forbidden = [(7, 'F')]
tooNear = [('E', 'F'), ('F', 'E')]
machPens = [[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,1,1],[1,1,1,1,1,1,10,20],[1,1,1,1,1,1,10,30],[1,1,1,1,1,10,1,1],]
tooNearPens = []


main = do
    let all = permutations "ABCDEFGH"
    let forcedAcc = getFAccepted forced forbidden all []
    let allAccepted = getTooNearAccepted tooNear forcedAcc []
    --print tooNearAccepted
    --findBestMatch 
    let bestMatch = findBestMatch allAccepted tooNearPens machPens 2147483647 []
    print bestMatch
    -- let machinePenalties = getMachinePenalties machPens "ABCDEFGH" 0
    -- let tooNearPenalties = getTooNearPenalties tooNearPens "ABCDEFGH" machinePenalties
    -- print tooNearPenalties


-- machine = “ABCDEFGH”


-- findIndex1 :: [(Integer, Char)] -> [Char] -> Integer -> Integer
-- findIndex1 tuple [] index = -1
-- findIndex1 tuple (x:xs) index 
--     | get2nd(tuple) == x = index
--     | otherwise = findIndex1 tuple xs (index + 1)


stringIsAccepted :: [(Int, Char)] -> [Char] -> Bool
stringIsAccepted [] machine = True
stringIsAccepted (x:xs) machine = (((elemIndices(snd(x)) machine) == [fst(x) -1]) && stringIsAccepted xs machine)

stringIsNotForbidden  :: [(Int, Char)] -> [Char]-> Bool
stringIsNotForbidden [] machine = True
stringIsNotForbidden (x:xs) machine = (((elemIndices(snd(x)) machine) /= [fst(x) -1]) && stringIsNotForbidden xs machine)


-- Takes in the forced penalties, the set of all possible strings, the set of all allowed strings so far, and returns a set of all allowed strings
getFAccepted :: [(Int, Char)] -> [(Int, Char)] -> [[Char]] -> [[Char]] -> [[Char]]

getFAccepted (x:xs) (w:ws) [] accepted = accepted
getFAccepted (x:xs) (w:ws) (y:ys) accepted
    | stringIsAccepted (x:xs) y && stringIsNotForbidden (w:ws) y = getFAccepted (x:xs) ys accepted ++ [y]
    | otherwise = getFAccepted (x:xs) ys accepted


-- Takes in too near tasks and machine string, returns bool.
stringTooNear :: [(Char, Char)] -> [Char] -> Bool
stringTooNear [] machine = True
--Checks if indices are 7 and 0, checks if indices are x and x-1, checks rest of too near
stringTooNear (x:xs) machine = (stringTooNear xs machine) && not (elemIndices(fst(x)) machine == [7] && elemIndices(snd(x)) machine == [0]) && not (elemIndices(fst(x)) machine!!0 == (elemIndices(snd(x)) machine!!0)-1)


-- Takes in the too near tasks, the set of all possible forced strings, the set of all allowed too near tasks and returns the set of all allowed strings recursively
getTooNearAccepted :: [(Char, Char)] -> [[Char]] -> [[Char]] -> [[Char]]

getTooNearAccepted (x:xs) [] accepted = accepted
getTooNearAccepted (x:xs) (y:ys) accepted
    | stringTooNear (x:xs) y = getTooNearAccepted (x:xs) ys accepted ++ [y]
    | otherwise = getTooNearAccepted (x:xs) ys accepted


--takes in a 2D array of integers, the machine string, and the total penalty so far and returns the total machine penalty
getMachinePenalties :: [[Int]] -> [Char] -> Int -> Int
stringRef = "ABCDEFGH"
getMachinePenalties matrix [] pen = 0
getMachinePenalties matrix (y:ys) pen = (getMachinePenalties matrix ys pen) + (matrix!!(8 - length (y:ys)))!!((elemIndices y stringRef)!!0)


extractFirst :: (a, b, c) -> a
extractFirst (a,_,_) = a

extractSecond :: (a, b, c) -> b
extractSecond (_,b,_) = b

extractThird :: (a, b, c) -> c
extractThird (_,_,c) = c


getTooNearPenalties :: [(Char, Char, Int)] -> [Char] -> Int -> Int
getTooNearPenalties [] machine pen = pen
getTooNearPenalties (x:xs) machine pen
    | (elemIndices(extractFirst(x)) machine == [7] && elemIndices(extractSecond(x)) machine == [0]) = (getTooNearPenalties xs machine pen) + extractThird(x)
    | (elemIndices(extractFirst(x)) machine!!0 == (elemIndices(extractSecond(x)) machine!!0)-1) = (getTooNearPenalties xs machine pen) + extractThird(x)
    | otherwise = getTooNearPenalties xs machine pen


findBestMatch :: [[Char]] -> [(Char, Char, Int)] -> [[Int]] -> Int -> [Char] ->[Char]
findBestMatch [] tooNearPenalties matrix tot best = best
findBestMatch (x:xs) tooNearPenalties matrix tot best
    | getTooNearPenalties tooNearPenalties x (getMachinePenalties matrix x 0) <= tot = findBestMatch xs tooNearPenalties matrix (getTooNearPenalties tooNearPenalties x (getMachinePenalties matrix x 0)) x
    | otherwise = findBestMatch xs tooNearPenalties matrix tot best






