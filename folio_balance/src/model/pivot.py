from model.geometry.vector import X, Y, Vector
from model.placement import Placement


class Pivot:
    def __init__(self, placement: Placement):
        self.placement = placement
        # Default orientation is orthogonal to the placement orientation
        # It has to be either along X or Y axis
        self.default_orientation = X if abs(placement.orientation.cross(X).magnitude()) > 0.9 else Y
        self.current_orientation = self.default_orientation
   
    # Rotate the pivot's current orientation by 90 degrees around the placement orientation 
    def rotate(self):
        self.current_orientation = self.current_orientation.cross(self.placement.orientation).get_unit_vector()
        # if current_orientation is along default_orientation, return False - else return True
        return self.current_orientation.dot(self.default_orientation) < 0.9