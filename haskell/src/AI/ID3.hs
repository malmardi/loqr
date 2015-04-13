module ID3 (
    ID3,
    create
) where

import Data.Vector.Unboxed as V

-- | Create an ID3 based classification tree
create ::  -- ^ The input attributes
       -> String 
 
create [] = 
