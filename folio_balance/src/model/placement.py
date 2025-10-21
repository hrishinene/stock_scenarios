from model.geometry.point import Point
from model.geometry.vector import Vector


class Placement:
    def __init__(self, point:Point, orientation:Vector):
        self.point = point
        self.orientation = orientation
        
    
